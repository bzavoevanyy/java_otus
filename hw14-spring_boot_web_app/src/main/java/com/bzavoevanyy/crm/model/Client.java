package com.bzavoevanyy.crm.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;
import java.util.Set;

@Table("client")
@Data
@Builder
public class Client {

    @Id
    @Nullable
    private final Long id;

    private final String name;

    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;
}
