package lk.ijse.dep7.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDTO implements Serializable {

    private String code;
    private String description;
    private BigDecimal price;
    private int qtyOnHand;


    public ItemDTO() {
    }

    public ItemDTO(String code, String description, BigDecimal price, int qtyOnHand) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.qtyOnHand = qtyOnHand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", qtyOnHand=" + qtyOnHand +
                '}';
    }
}
