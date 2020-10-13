package com.mvinuesa.stream.mammal.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * Model Mammal
 *
 * @since 1.0.0
 * @author mvinuesa
 */
@Data
public class Mammal {

    @JsonAlias({"catId", "dogId"})
    String id;
}
