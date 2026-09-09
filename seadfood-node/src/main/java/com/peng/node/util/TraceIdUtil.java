package com.peng.node.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 溯源标识码生成工具类
 * 用于生成零售商溯源标识码 source_id，消费者通过该码查询溯源链路
 */
public class TraceIdUtil {

    /**
     * 生成唯一溯源标识码source_id
     * UUID去掉横杠后截取前10位大写字符串，最大长度10，适配VARCHAR(20)
     * @return 溯源标识码，例："F2E8D4A17B3C449092AB"
     */
    public static String generateSourceId() {
        String uuidStr = UUID.randomUUID().toString();
        // 去掉横杠，转为大写，截取前10位，严格控制长度<=10
        String raw = uuidStr.replace("-", "").toUpperCase();
        return raw.substring(0, 10);
    }

    /**
     * 根据溯源编号生成二维码图片，返回Base64字符串（不带data:image前缀）
     * @param sourceId 溯源标识码
     * @return 二维码Base64字符串
     */
    public static String generateQrCodeBase64(String sourceId) {
        int width = 300;
        int height = 300;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(sourceId, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            byte[] bytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("二维码生成失败", e);
        }
    }
}
