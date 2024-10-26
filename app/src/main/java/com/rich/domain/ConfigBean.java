package com.rich.domain;

import lombok.Data;

/**
 * @author rich
 * @date 2024/10/25
 * @description
 */
@Data
public class ConfigBean {

    private String packageName;

    private String viewId;

    private String viewText;

    private PixelCoordinates location;

    @Data
    public static class PixelCoordinates {
        private int x;
        private int y;
    }

}
