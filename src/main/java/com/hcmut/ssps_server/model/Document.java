package com.hcmut.ssps_server.model;

import com.hcmut.ssps_server.model.user.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "document")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    String fileType;

    @Column(nullable = false)
    int pageCount;

    @Column(nullable = false)
    String paperSize;

    @Column(nullable = false)
    String sidedType;

    @Column(nullable = false)
    int numberOfCopies;

    @Column(nullable = false)
    boolean isColorPrint;
}
