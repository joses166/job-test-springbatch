package br.com.josehamilton.jobtestspringbatch.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {

    public Long id;
    public String qualifiedName;
    public String model;

}
