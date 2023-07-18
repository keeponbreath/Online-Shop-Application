package spring.cloud.itemservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.cloud.itemservice.enums.ItemStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private Long barCode;
    private Long orgId;
    private List<String> tags;
    private Long discountId;
    private String name;
    private String description;
    private Double price;
    private ItemStatus status;
}
