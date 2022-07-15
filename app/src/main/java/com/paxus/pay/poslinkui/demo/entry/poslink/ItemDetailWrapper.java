package com.paxus.pay.poslinkui.demo.entry.poslink;

import java.io.Serializable;

/**
 * Created by Yanina.Yang on 7/15/2022.
 */
class ItemDetailWrapper {
    private String index;
    private ItemDetail itemDetail;

    public ItemDetailWrapper() {
    }

    public ItemDetailWrapper(String index, ItemDetail itemDetail) {
        this.index = index;
        this.itemDetail = itemDetail;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    public static class ItemDetail implements Serializable {

        private String productName;
        private String plUcode;
        private double price;
        private String unit;
        private double unitPrice;
        private String tax;
        private String quantity;
        private String productImgUri;
        private String productImgDesc;


        public ItemDetail(String productName, String plUcode, double price, String unit, double unitPrice, String tax, String quantity, String productImgUri, String productImgDesc) {
            this.productName = productName;
            this.plUcode = plUcode;
            this.price = price;
            this.unit = unit;
            this.unitPrice = unitPrice;
            this.tax = tax;
            this.quantity = quantity;
            this.productImgUri = productImgUri;
            this.productImgDesc = productImgDesc;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPlUcode() {
            return plUcode;
        }

        public void setPlUcode(String plUcode) {
            this.plUcode = plUcode;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getProductImgUri() {
            return productImgUri;
        }

        public void setProductImgUri(String productImgUri) {
            this.productImgUri = productImgUri;
        }

        public String getProductImgDesc() {
            return productImgDesc;
        }

        public void setProductImgDesc(String productImgDesc) {
            this.productImgDesc = productImgDesc;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ItemDetail{");
            sb.append("productName='").append(productName).append('\'');
            sb.append(", plUcode='").append(plUcode).append('\'');
            sb.append(", price=").append(price);
            sb.append(", unit='").append(unit).append('\'');
            sb.append(", unitPrice=").append(unitPrice);
            sb.append(", tax='").append(tax).append('\'');
            sb.append(", quantity='").append(quantity).append('\'');
            sb.append(", productImgUri='").append(productImgUri).append('\'');
            sb.append(", productImgDesc='").append(productImgDesc).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
