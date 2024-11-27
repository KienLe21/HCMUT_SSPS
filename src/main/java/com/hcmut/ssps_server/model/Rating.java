package com.hcmut.ssps_server.model;

import com.hcmut.ssps_server.model.user.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "rating")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    int rating;

    @Column(nullable = false)
    String comment;

    @ManyToOne
    @JoinColumn(name = "printinglog_id", nullable = false)
    PrintingLog printingLog;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;
}
