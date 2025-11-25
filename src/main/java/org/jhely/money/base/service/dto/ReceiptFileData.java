package org.jhely.money.base.service.dto;

public record ReceiptFileData(String filename, String contentType, byte[] data) {}
