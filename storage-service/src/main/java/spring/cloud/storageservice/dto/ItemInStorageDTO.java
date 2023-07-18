package spring.cloud.storageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.cloud.storageservice.entity.ItemInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInStorageDTO {
    private ItemInfo itemInfo;
    private Long count;
}
