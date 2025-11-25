package org.jhely.money.base.ui.view;

import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileInputStream;
import java.text.DecimalFormat;
 

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.ReceiptFile;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.ExtractionWorker;
import org.jhely.money.base.service.ReceiptService;
import org.jhely.money.base.repository.ReceiptFileSummary;
import com.vaadin.flow.data.provider.DataProvider;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RolesAllowed("USER")
@Route(value = "receipts", layout = MainLayout.class)
@PageTitle("Receipts")
public class ReceiptsView extends VerticalLayout {

  private final ReceiptService receiptService;
  private final ExtractionWorker extractionWorker;
  private final AuthenticatedUser auth;

  private final Grid<ReceiptFileSummary> grid = new Grid<>(ReceiptFileSummary.class, false);
  private final SplitLayout split = new SplitLayout();
  private final Div details = new Div();
  private final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  // Upload cap (match service)
  private static final int MAX_FILE_BYTES = 10 * 1024 * 1024;

  // --- Pagination state ---
  // Server-side paging via Vaadin DataProvider

  public ReceiptsView(ReceiptService receiptService, AuthenticatedUser auth, ExtractionWorker extractionWorker) {
    this.receiptService = receiptService;
    this.auth = auth;
    this.extractionWorker = extractionWorker;

    setSizeFull();
    setSpacing(false);
    setPadding(true);

    // Create a content wrapper that will handle scrolling
    VerticalLayout content = new VerticalLayout();
    content.setSizeFull();
    content.setPadding(false);
    content.setSpacing(true);
    content.getStyle().set("overflow", "auto");
    content.setHeight("100%");
    
    content.add(buildUploadBar(), buildMasterDetail());
	content.getStyle().set(
		"padding-bottom",
		"calc(var(--app-bottom-bar-height, 64px) + var(--lumo-space-m))"
		);


    add(content);
    
    refresh();
  }

  private Component buildMasterDetail() {
    // Left: Grid (+ pager bar below)
    buildGrid();
  VerticalLayout primary = new VerticalLayout(grid);
    primary.setPadding(false);
    primary.setSpacing(false);
    primary.setSizeFull();
    primary.setFlexGrow(1, grid);
	grid.setHeight("auto");

    // Right: details placeholder
    details.setSizeFull();
    details.getStyle().set("padding", "var(--lumo-space-m)");
    clearDetails();

    // Split
    split.setOrientation(SplitLayout.Orientation.HORIZONTAL);
    split.addToPrimary(primary);
    split.addToSecondary(details);
    split.setSizeFull();
    split.setSplitterPosition(48);

    // Selection
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
  grid.addSelectionListener(ev -> ev.getFirstSelectedItem().ifPresentOrElse(
    summary -> this.showDetails(fetchFull(summary)),
    this::clearDetails
  ));

    // Server-side paging: fetch counts/items on demand
    grid.setItems(DataProvider.fromCallbacks(
        query -> {
          var user = auth.get().orElse(null);
          if (user == null) return Stream.<ReceiptFileSummary>empty();
          int offset = query.getOffset();
          int limit = query.getLimit();
          int page = limit == 0 ? 0 : offset / limit;
          Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "uploadedAt"));
          return receiptService.pageSummariesForOwner(user.getEmail(), pageable).stream();
        },
        query -> {
          var user = auth.get().orElse(null);
          return user == null ? 0 : (int) receiptService.countForOwner(user.getEmail());
        }
    ));

    return split;
  }

  // Pager bar removed in favor of server-side paging via DataProvider

  private void clearDetails() {
    details.removeAll();
    details.add(new Paragraph("Select a receipt to see details."));
  }

  private void showDetails(ReceiptFile r) {
    details.removeAll();

    H3 title = new H3(r.getFilename());
    Span meta = small(humanSize(r.getSizeBytes()) + " • " +
        (r.getContentType() != null ? r.getContentType() : "unknown"));
  var actionsBar = detailActions(r);

    var tabs = new TabSheet();
    tabs.setSizeFull();
  tabs.add("Preview", buildPreview(r));
    tabs.add("Extraction", buildExtractionPanel(r));

    details.add(title, meta, new Div(actionsBar), tabs);
  }

  private Component buildPreview(ReceiptFile r) {
    String fileName = r.getFilename();
    String mime = r.getContentType() != null ? r.getContentType() : "application/octet-stream";

    // Images inline via URL (avoid loading bytes in the grid/list)
    if (mime.startsWith("image/")) {
      String url = "/api/receipts/" + r.getId() + "?ownerEmail=" + r.getOwnerEmail();
      Image img = new Image(url, "preview");
      img.setMaxWidth("100%");
      img.setMaxHeight("80vh");
      img.getStyle().set("object-fit", "contain");
      Div wrap = new Div(img);
      wrap.getStyle().set("display", "grid").set("place-items", "center");
      wrap.setSizeFull();
      return wrap;
    }

    // PDF inline via <object>
    if ("application/pdf".equalsIgnoreCase(mime)) {
      String url = "/api/receipts/" + r.getId() + "?ownerEmail=" + r.getOwnerEmail();
      Div container = new Div();
      container.getElement().setProperty("innerHTML",
          "<object data='" + url + "' type='application/pdf' width='100%' height='600px'>" +
              "  <p>PDF cannot be displayed. <a href='" + url + "' target='_blank'>Download here</a>.</p>" +
              "</object>"
      );
      return container;
    }

    // Fallback: download link (+ optional text peek by fetching only on demand)
    String url = "/api/receipts/" + r.getId() + "?ownerEmail=" + r.getOwnerEmail();
    Anchor a = new Anchor(url, "Download " + fileName);
    a.getElement().setAttribute("download", true);

    VerticalLayout box = new VerticalLayout(a);
    box.setPadding(false);
    box.setSpacing(true);
    box.setSizeFull();

    // Optional small text preview: this would require fetching bytes; keep minimal to avoid heavy loads
    return box;
  }

  private Span small(String text) {
    Span s = new Span(text == null ? "" : text);
    s.getStyle()
        .set("color", "var(--lumo-secondary-text-color)")
        .set("font-size", "var(--lumo-font-size-s)");
    return s;
  }

  private Component buildExtractionPanel(ReceiptFile r) {
    var wrapper = new VerticalLayout();
    wrapper.setPadding(false);
    wrapper.setSpacing(true);
    wrapper.setSizeFull();

    var opt = receiptService.findExtractionForReceipt(r.getId());
    if (opt.isEmpty()) {
      wrapper.add(new Paragraph("No extraction available yet."));
      return wrapper;
    }

    var ex = opt.get();

    // Status
    Span status = small("Status: " + ex.getStatus()
        + (ex.getUpdatedAt() != null ? " • " + ex.getUpdatedAt() : ""));
    wrapper.add(status);

    // Summary facts using your denormalized columns
    FormLayout facts = new FormLayout();
    facts.addFormItem(ro(ex.getSupplierName()), "Supplier");
    facts.addFormItem(ro(ex.getSupplierTaxId()), "Supplier Tax ID");
    facts.addFormItem(ro(ex.getInvoiceNumber()), "Invoice #");
    facts.addFormItem(ro(ex.getIssueDate() != null ? ex.getIssueDate().toString() : ""), "Issue date");
    facts.addFormItem(ro(ex.getCurrency()), "Currency");
    facts.addFormItem(ro(ex.getTotal() != null ? ex.getTotal().toPlainString() : ""), "Total");
    facts.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1),
        new FormLayout.ResponsiveStep("700px", 2)
    );

    // Raw payloadJson pretty-printed
    TextArea json = new TextArea("Raw payload");
    json.setWidthFull();
    json.setHeight("300px");
    json.setReadOnly(true);
    try {
      String raw = ex.getPayloadJson();
      if (raw != null && !raw.isBlank()) {
        try {
          var tree = mapper.readTree(raw);
          json.setValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
        } catch (Exception notJson) {
          json.setValue(raw); // store as-is if not valid JSON
        }
      } else {
        json.setValue("{}");
      }
    } catch (Exception err) {
      json.setValue("{ \"error\": \"" + err.getMessage() + "\" }");
    }

    wrapper.add(facts, json);
    return wrapper;
  }

  private TextField ro(String value) {
    TextField tf = new TextField();
    tf.setValue(value == null ? "" : value);
    tf.setReadOnly(true);
    tf.setWidthFull();
    return tf;
  }

  private HorizontalLayout buildUploadBar() {
    var fileBuffer = new FileBuffer(); // writes client uploads to a temp file
    var upload = new Upload(fileBuffer);

    upload.setDropLabel(new Span("Drop receipts here"));
    upload.setMaxFiles(50);
    upload.setMaxFileSize(MAX_FILE_BYTES);
    upload.setAcceptedFileTypes(
        // all images
        "image/*",
        // documents
        "application/pdf", ".pdf",
        "application/msword", ".doc",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx",
        "application/rtf", ".rtf",
        "text/plain", ".txt",
        // emails
        "message/rfc822", ".eml",
        "application/vnd.ms-outlook", ".msg",
        // common extras
        ".heic", ".tif", ".tiff", ".bmp", ".webp", ".svg"
    );

    upload.addFailedListener(e ->
        Notification.show("Upload failed: " + (e.getReason() != null ? e.getReason().getMessage() : "Unknown"),
            5000, Notification.Position.MIDDLE));

    upload.addSucceededListener(event -> {
      var currentUser = auth.get().orElse(null);
      if (currentUser == null) {
        Notification.show("You are not logged in");
        return;
      }

      var tmp = fileBuffer.getFileData().getFile();
      var filename = event.getFileName();
      var contentType = event.getMIMEType();
      var size = event.getContentLength();

      // store to DB
      try (var in = new FileInputStream(tmp)) {
        var saved = receiptService.save(currentUser.getEmail(), filename, contentType, size, in);
        extractionWorker.process(saved.getId(), currentUser.getEmail()); // @Async
        Notification.show("Uploaded: " + filename);
        refresh();
      } catch (Exception ex) {
        Notification.show("Failed to store " + filename + ": " + ex.getMessage(), 5000,
            Notification.Position.MIDDLE);
      } finally {
        if (tmp != null) tmp.delete();
      }
    });

    upload.addFileRejectedListener(
        e -> Notification.show("Rejected: " + e.getErrorMessage(), 5000, Notification.Position.MIDDLE));

    var help = new Span("Max 10MB per file. Images & PDFs recommended.");
    var bar = new HorizontalLayout(upload, help);
    bar.setAlignItems(Alignment.CENTER);
    bar.setWidthFull();
    return bar;
  }

  private Grid<ReceiptFileSummary> buildGrid() {
    grid.addThemeName("row-stripes");

    // Thumbnail placeholder (icon only to avoid loading image bytes in the list)
    grid.addComponentColumn(item -> {
        var icon = VaadinIcon.FILE_TEXT_O.create();
        icon.setSize("20px");
        return icon;
      })
      .setHeader("")
      .setAutoWidth(true)
      .setFlexGrow(0)
      .setWidth("64px");

    // File name (flex)
  grid.addColumn(ReceiptFileSummary::getFilename)
        .setHeader("File name")
        .setFlexGrow(1)
        .setAutoWidth(true);

    // Size (compact, right-aligned)
    grid.addColumn(r -> humanSize(r.getSizeBytes()))
        .setHeader("Size")
        .setAutoWidth(false)
        .setWidth("110px")
        .setFlexGrow(0)
        .setTextAlign(ColumnTextAlign.END);

    // Uploaded (compact)
    grid.addColumn(r -> r.getUploadedAt()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        .setHeader("Uploaded")
        .setAutoWidth(false)
        .setWidth("160px")
        .setFlexGrow(0);

    grid.addComponentColumn(this::actions)
        .setHeader("Actions")
        .setAutoWidth(true)
        .setFlexGrow(0);

    return grid;
  }

  private Component actions(ReceiptFileSummary r) {
    // Download via endpoint URL to avoid loading bytes in-memory
    String url = "/api/receipts/" + r.getId() + "?ownerEmail=" + r.getOwnerEmail();
    Anchor download = new Anchor(url, "");
    download.getElement().setAttribute("download", true);

    Button downloadBtn = new Button("Download", VaadinIcon.DOWNLOAD.create());
    downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    downloadBtn.getElement().setProperty("title", "Download " + r.getFilename());
    download.add(downloadBtn);

    Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create(), e -> {
      var user = auth.get().orElse(null);
      if (user == null) return;
      receiptService.delete(user.getEmail(), r.getId());
      refresh();
    });
    deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_SMALL);
    deleteBtn.getElement().setProperty("title", "Delete " + r.getFilename());

    var row = new HorizontalLayout(download, deleteBtn);
    row.setSpacing(true);
    row.setPadding(false);
    row.setAlignItems(FlexComponent.Alignment.CENTER);
    return row;
  }

  private Component detailActions(ReceiptFile r) {
    String url = "/api/receipts/" + r.getId() + "?ownerEmail=" + r.getOwnerEmail();
    Anchor download = new Anchor(url, "");
    download.getElement().setAttribute("download", true);

    Button downloadBtn = new Button("Download", VaadinIcon.DOWNLOAD.create());
    downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    downloadBtn.getElement().setProperty("title", "Download " + r.getFilename());
    download.add(downloadBtn);

    Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create(), e -> {
      var user = auth.get().orElse(null);
      if (user == null) return;
      receiptService.delete(user.getEmail(), r.getId());
      refresh();
    });
    deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_SMALL);
    deleteBtn.getElement().setProperty("title", "Delete " + r.getFilename());

    var row = new HorizontalLayout(download, deleteBtn);
    row.setSpacing(true);
    row.setPadding(false);
    row.setAlignItems(FlexComponent.Alignment.CENTER);
    return row;
  }

  // Thumbnail removed in list to prevent accidental BLOB loads

  private void refresh() {
    // With server-side paging, trigger a re-fetch
    grid.getDataProvider().refreshAll();

  }

  // Client-side page update removed; server-side paging in use

  private static String humanSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    double kb = bytes / 1024.0;
    if (kb < 1024) return new DecimalFormat("#,##0.#").format(kb) + " KB";
    double mb = kb / 1024.0;
    return new DecimalFormat("#,##0.##").format(mb) + " MB";
  }

  private ReceiptFile fetchFull(ReceiptFileSummary summary) {
    var user = auth.get().orElse(null);
    if (user == null) return null;
    return receiptService.getByIdForOwnerOrThrow(summary.getId(), user.getEmail());
  }
}
