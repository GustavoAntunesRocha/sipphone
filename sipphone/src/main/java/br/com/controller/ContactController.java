package br.com.controller;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.model.Contact;

public class ContactController {
    
    public ContactController() {
        
    }

    public void addContact(String name, String phoneNumber, String email) {
        // TODO Auto-generated method stub
        Contact contact = new Contact(name, phoneNumber, email);
        App.acc.getContacts().add(contact);
        AccountEntity.writeAccountToFile(App.acc);
    }
}
