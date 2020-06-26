package com.hust.baseweb.applications.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportInventoryItemsInputModel {

    private ImportInventoryItemInputModel[] inventoryItems;
}
