package com.gautama.learnreactivespring.postgres.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table(value = "product")
public class Product{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;
    String title;
    Double price;
}
