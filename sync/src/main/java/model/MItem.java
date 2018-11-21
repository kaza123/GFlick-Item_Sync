/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author kasun
 */
public class MItem {
    private String barcode;
    private String name;
    private String printDescription;
    private String size;
    private BigDecimal unitPrice;
    private String department;
    private String category;
    private String style;
    private String styleRepeat;

    public MItem() {
    }

    public MItem(String barcode, String name, String printDescription, String size, BigDecimal unitPrice, String department, String category, String style, String styleRepeat) {
        this.barcode = barcode;
        this.name = name;
        this.printDescription = printDescription;
        this.size = size;
        this.unitPrice = unitPrice;
        this.department = department;
        this.category = category;
        this.style = style;
        this.styleRepeat = styleRepeat;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintDescription() {
        return printDescription;
    }

    public void setPrintDescription(String printDescription) {
        this.printDescription = printDescription;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleRepeat() {
        return styleRepeat;
    }

    public void setStyleRepeat(String styleRepeat) {
        this.styleRepeat = styleRepeat;
    }

    @Override
    public String toString() {
        return "barcode "+this.barcode+
                "category "+this.category+
                "department "+this.department+
                "name "+this.name+
                "printDescription "+this.printDescription+
                "size "+this.size+
                "style "+this.style+
                "styleRepeat "+this.styleRepeat;
    }

   
}
