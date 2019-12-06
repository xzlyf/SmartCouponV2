package com.xz.jlw2.entity;

public class DailyStringEntity {


    private String tts;//音频地址
    private String content;//英文内容
    private String note;//中文内容
    private String love;//每日一句喜欢个数
    private String translation;//词霸小编语录
    private String picture;//图片地址
    private String picture2;//大图片地址
    private String caption;//标题
    private String dateline;//时间
    private String localTtspath;//已下载好的音频文件本地地址

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getLocalTtspath() {
        return localTtspath;
    }

    public void setLocalTtspath(String localTtspath) {
        this.localTtspath = localTtspath;
    }
}

