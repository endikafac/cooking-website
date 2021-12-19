package com.cookingwebsite.crud.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    @NotBlank
    private String nombre;
    @Min(0)
    private Float precio;


}
