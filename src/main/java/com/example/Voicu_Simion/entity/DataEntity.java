package com.example.Voicu_Simion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map; // <--- Import nou, standard Java

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "data")
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Am schimbat JsonNode cu Map.
    // Hibernate știe automat să pună JSON-ul din bază aici.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "json")
    private Map<String, Object> data;

}