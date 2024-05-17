package com.steph.onlinecollegeelectionscentre.v1.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.event.spi.PreInsertEvent;

@Getter
@Setter
public class AddVoterModel {

    private String username;

    private String password;

    private String firstName;

    private String lastName;
}
