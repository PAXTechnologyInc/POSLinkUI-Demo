package com.paxus.pay.poslinkui.demo.entry.text.fleet;

/**
 * ClassName:FleetData
 * Description:
 * date:2025/12/8
 */
public class FleetData {
    private int titleId;
    private String defaultValuePattern;
    private String requestedParamName;
    private String defaultInputType;

    public FleetData(int titleId, String defaultValuePattern, String requestedParamName, String defaultInputType) {
        this.titleId = titleId;
        this.defaultValuePattern = defaultValuePattern;
        this.requestedParamName = requestedParamName;
        this.defaultInputType = defaultInputType;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitle(int titleId) {
        this.titleId = titleId;
    }

    public String getDefaultValuePattern() {
        return defaultValuePattern;
    }

    public void setDefaultValuePattern(String defaultValuePattern) {
        this.defaultValuePattern = defaultValuePattern;
    }

    public String getRequestedParamName() {
        return requestedParamName;
    }

    public void setRequestedParamName(String requestedParamName) {
        this.requestedParamName = requestedParamName;
    }

    public String getDefaultInputType() {
        return defaultInputType;
    }

    public void setDefaultInputType(String defaultInputType) {
        this.defaultInputType = defaultInputType;
    }
}
