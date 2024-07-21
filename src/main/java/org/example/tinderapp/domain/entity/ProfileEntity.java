package org.example.tinderapp.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class ProfileEntity {
    private Long id;
    private String name;
    private String imageUrl;
    private Long userId;
}
