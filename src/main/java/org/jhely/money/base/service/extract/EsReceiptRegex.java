package org.jhely.money.base.service.extract;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EsReceiptRegex {

  private EsReceiptRegex() {}

  // -------------------------------
  // NIF / NIE / CIF patterns
  // -------------------------------
  // NIF: 8 digits + letter (control)   e.g., 12345678Z
  public static final Pattern NIF_PATTERN =
      Pattern.compile("\\b(\\d{8})([TRWAGMYFPDXBNJZSQVHLCKE])\\b", Pattern.CASE_INSENSITIVE);

  // NIE: X/Y/Z + 7 digits + letter     e.g., X1234567L
  public static final Pattern NIE_PATTERN =
      Pattern.compile("\\b([XYZ])(\\d{7})([TRWAGMYFPDXBNJZSQVHLCKE])\\b", Pattern.CASE_INSENSITIVE);

  // CIF: 1 letter + 7 digits + control (digit or letter)
  //      e.g., B12345678, J1234567B, etc.
  public static final Pattern CIF_PATTERN =
      Pattern.compile("\\b([ABCDEFGHJKLMNPQRSUVW])\\s?(\\d{7})\\s?([A-J0-9])\\b", Pattern.CASE_INSENSITIVE);

  // -------------------------------
  // Dates
  // -------------------------------
  // Numeric dates: 31/12/2025, 31-12-2025, 2025-12-31, 31.12.2025
  public static final Pattern DATE_NUMERIC_PATTERN = Pattern.compile(
      "\\b(?:(?<d>\\d{1,2})[\\./-](?<m>\\d{1,2})[\\./-](?<y>\\d{2,4})|(?<y2>\\d{4})[\\./-](?<m2>\\d{1,2})[\\./-](?<d2>\\d{1,2}))\\b");

  // Spanish long dates: "31 de enero de 2025", "1 de MARZO de 25"
  private static final String MONTHS_ES =
      "(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|setiembre|octubre|noviembre|diciembre)";
  public static final Pattern DATE_LONG_ES_PATTERN = Pattern.compile(
      "\\b(?<d>\\d{1,2})\\s+de\\s+(?<mes>" + MONTHS_ES + ")\\s+de\\s+(?<y>\\d{2,4})\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

  private static final Map<String, Integer> MONTH_ES_TO_NUM = Map.ofEntries(
      Map.entry("enero", 1), Map.entry("febrero", 2), Map.entry("marzo", 3), Map.entry("abril", 4),
      Map.entry("mayo", 5), Map.entry("junio", 6), Map.entry("julio", 7), Map.entry("agosto", 8),
      Map.entry("septiembre", 9), Map.entry("setiembre", 9), Map.entry("octubre", 10), Map.entry("noviembre", 11),
      Map.entry("diciembre", 12)
  );

  // -------------------------------
  // Amounts / totals
  // -------------------------------
  // Matches 1.234,56  |  1,234.56  |  1234,56  |  1234.56  with optional currency (€/$/£) around it.
  public static final Pattern AMOUNT_PATTERN = Pattern.compile(
      "(?<!\\w)(?:[€$£]\\s*)?([+-]?(?:\\d{1,3}(?:[.,]\\d{3})+|\\d+)(?:[.,]\\d{2})?)(?:\\s*[€$£])?(?!\\w)");

  // Lines that likely contain totals
  public static final Pattern TOTAL_LINE_HINT = Pattern.compile(
      "(total(\\s*a\\s*pagar)?|importe\\s*total|total\\s*\\(iva\\s*incluido\\)|amount\\s*due|grand\\s*total)",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

  // Quick currency hint
  public static String detectCurrency(String text) {
    String n = normalize(text);
    if (n.contains("€") || n.contains(" eur ") || n.contains(" euro")) return "EUR";
    if (n.contains(" usd ") || n.contains("$")) return "USD";
    if (n.contains(" gbp ") || n.contains("£") || n.contains(" pound")) return "GBP";
    return "EUR"; // default for Spain
  }

  // -------------------------------
  // Public helpers
  // -------------------------------
  public static Optional<String> findFirstTaxId(String text) {
    Matcher m1 = NIF_PATTERN.matcher(text);
    if (m1.find() && isValidNif(m1.group())) return Optional.of(m1.group());

    Matcher m2 = NIE_PATTERN.matcher(text);
    if (m2.find() && isValidNie(m2.group())) return Optional.of(m2.group());

    Matcher m3 = CIF_PATTERN.matcher(text);
    if (m3.find() && isValidCif(m3.group())) return Optional.of(m3.group());

    return Optional.empty();
  }

  public static List<String> findAllTaxIds(String text) {
    List<String> out = new ArrayList<>();
    Matcher m1 = NIF_PATTERN.matcher(text);
    while (m1.find()) if (isValidNif(m1.group())) out.add(m1.group());
    Matcher m2 = NIE_PATTERN.matcher(text);
    while (m2.find()) if (isValidNie(m2.group())) out.add(m2.group());
    Matcher m3 = CIF_PATTERN.matcher(text);
    while (m3.find()) if (isValidCif(m3.group())) out.add(m3.group());
    return out;
  }

  public static List<LocalDate> findDates(String text) {
    String txt = normalize(text);
    List<LocalDate> out = new ArrayList<>();

    // Numeric
    Matcher m = DATE_NUMERIC_PATTERN.matcher(txt);
    while (m.find()) {
      try {
        if (m.group("d") != null) {
          int d = Integer.parseInt(m.group("d"));
          int mo = Integer.parseInt(m.group("m"));
          int y = fixYear(m.group("y"));
          out.add(LocalDate.of(y, mo, d));
        } else {
          int y = Integer.parseInt(m.group("y2"));
          int mo = Integer.parseInt(m.group("m2"));
          int d = Integer.parseInt(m.group("d2"));
          out.add(LocalDate.of(y, mo, d));
        }
      } catch (Exception ignore) {}
    }

    // Long Spanish
    Matcher m2 = DATE_LONG_ES_PATTERN.matcher(txt);
    while (m2.find()) {
      try {
        int d = Integer.parseInt(m2.group("d"));
        int mo = MONTH_ES_TO_NUM.getOrDefault(m2.group("mes").toLowerCase(Locale.ROOT), 0);
        int y = fixYear(m2.group("y"));
        if (mo >= 1) out.add(LocalDate.of(y, mo, d));
      } catch (Exception ignore) {}
    }

    // Remove duplicates
    return new ArrayList<>(new LinkedHashSet<>(out));
  }

  public static Optional<BigDecimal> findLikelyTotal(String text) {
    String[] lines = text.split("\\R");
    BigDecimal best = null;

    // 1) Prefer lines that look like TOTAL
    for (String line : lines) {
      if (TOTAL_LINE_HINT.matcher(line).find()) {
        BigDecimal maxInLine = maxAmountIn(line);
        if (maxInLine != null) {
          if (best == null || maxInLine.compareTo(best) > 0) best = maxInLine;
        }
      }
    }
    if (best != null) return Optional.of(best);

    // 2) Fallback: max amount anywhere
    BigDecimal max = null;
    Matcher all = AMOUNT_PATTERN.matcher(text);
    while (all.find()) {
      BigDecimal v = parseAmount(all.group(1));
      if (v != null) {
        if (max == null || v.compareTo(max) > 0) max = v;
      }
    }
    return Optional.ofNullable(max);
  }

  // -------------------------------
  // Validation (NIF/NIE/CIF)
  // -------------------------------
  public static boolean isValidNif(String raw) {
    String s = raw.toUpperCase(Locale.ROOT).replaceAll("\\s+", "");
    Matcher m = NIF_PATTERN.matcher(s);
    if (!m.matches()) return false;
    String num = m.group(1);
    char control = m.group(2).charAt(0);
    String table = "TRWAGMYFPDXBNJZSQVHLCKE";
    int idx = Integer.parseInt(num) % 23;
    return table.charAt(idx) == control;
  }

  public static boolean isValidNie(String raw) {
    String s = raw.toUpperCase(Locale.ROOT).replaceAll("\\s+", "");
    Matcher m = NIE_PATTERN.matcher(s);
    if (!m.matches()) return false;
    String prefix = m.group(1);
    String num = m.group(2);
    char control = m.group(3).charAt(0);
    // NIE: replace X/Y/Z with 0/1/2 and compute NIF control
    String mapped = switch (prefix) { case "X" -> "0"; case "Y" -> "1"; case "Z" -> "2"; default -> ""; } + num;
    String table = "TRWAGMYFPDXBNJZSQVHLCKE";
    int idx = Integer.parseInt(mapped) % 23;
    return table.charAt(idx) == control;
  }

  public static boolean isValidCif(String raw) {
    // Based on Agencia Tributaria rules
    String s = raw.toUpperCase(Locale.ROOT).replaceAll("\\s+", "");
    Matcher m = CIF_PATTERN.matcher(s);
    if (!m.matches()) return false;

    char type = m.group(1).charAt(0);
    String digits = m.group(2); // 7 digits
    char control = m.group(3).charAt(0);

    int sumEven = 0, sumOdd = 0;
    for (int i = 0; i < digits.length(); i++) {
      int d = digits.charAt(i) - '0';
      // positions are 1-based in the rule; here i=0 => pos1 (odd)
      if ((i % 2) == 0) { // odd positions: multiply by 2, sum digits
        int prod = d * 2;
        sumOdd += (prod / 10) + (prod % 10);
      } else {            // even positions: add as-is
        sumEven += d;
      }
    }
    int total = sumEven + sumOdd;
    int unit = total % 10;
    int checkDigit = (unit == 0) ? 0 : 10 - unit; // numeric control (0..9)
    char checkChar = "JABCDEFGHI".charAt(checkDigit); // letter control

    // Control type depends on first letter:
    //  A,B,E,H => must be digit
    //  K,P,Q,S,N,W => must be letter
    //  C,D,F,G,J,U,V,T,R,M => either
    if ("ABEH".indexOf(type) >= 0) {
      return control == (char)('0' + checkDigit);
    } else if ("KPQSNW".indexOf(type) >= 0) {
      return control == checkChar;
    } else {
      return control == checkChar || control == (char)('0' + checkDigit);
    }
  }

  // -------------------------------
  // Internals
  // -------------------------------
  private static String normalize(String t) {
    if (t == null) return "";
    String s = Normalizer.normalize(t, Normalizer.Form.NFKC);
    s = s.replace('\u00A0', ' '); // nbsp -> space
    return " " + s.toLowerCase(Locale.ROOT) + " ";
  }

  private static int fixYear(String yRaw) {
    int y = Integer.parseInt(yRaw);
    if (y < 100) {
      // two-digit year heuristic: 00..69 => 2000..2069, 70..99 => 1970..1999
      return (y <= 69) ? (2000 + y) : (1900 + y);
    }
    return y;
  }

  private static BigDecimal maxAmountIn(String line) {
    Matcher m = AMOUNT_PATTERN.matcher(line);
    BigDecimal max = null;
    while (m.find()) {
      BigDecimal v = parseAmount(m.group(1));
      if (v != null) {
        if (max == null || v.compareTo(max) > 0) max = v;
      }
    }
    return max;
  }

  public static BigDecimal parseAmount(String raw) {
    if (raw == null || raw.isBlank()) return null;
    String s = raw.trim();

    // Remove spaces and currency symbols
    s = s.replaceAll("[\\s€$£]", "");

    // Decide decimal separator:
    // if both ',' and '.' present: the rightmost symbol is decimal sep; the other is thousands
    int lastComma = s.lastIndexOf(',');
    int lastDot   = s.lastIndexOf('.');
    char decimalSep;
    if (lastComma == -1 && lastDot == -1) {
      // no separators -> integer
      decimalSep = '.'; // will not be used
      s = s;
    } else if (lastComma != -1 && lastDot != -1) {
      decimalSep = (lastComma > lastDot) ? ',' : '.';
      char thousandsSep = (decimalSep == ',') ? '.' : ',';
      s = s.replace(String.valueOf(thousandsSep), "");
      if (decimalSep == ',') s = s.replace(',', '.');
    } else if (lastComma != -1) {
      // only comma present -> assume comma decimal (EU)
      s = s.replace(".", ""); // stray dots as thousand
      s = s.replace(',', '.');
    } else {
      // only dot present -> assume dot decimal (US)
      s = s.replace(",", ""); // stray commas as thousand
    }

    try {
      return new BigDecimal(s);
    } catch (Exception e) {
      return null;
    }
  }

  // Utility to format LocalDate lists (optional)
  public static String formatDate(LocalDate d) {
    return d == null ? "" : d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
