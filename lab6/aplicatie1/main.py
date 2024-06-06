
class Contact:
    def __init__(self, name, email):  # constructor
        self.name = name
        self.email = email

    def __repr__(self):
        return "Contact({}, {})".format(self.name, self.email)


class ContactList(list):
    def search(self, name):
        matching_contacts = []
        for contact in self:
            if name in contact.name:
                matching_contacts.append(contact)
        return matching_contacts


class Agenda:
    all_contacts = ContactList()

    def add_contact(self, contact):
        self.all_contacts.append(contact)

    def print_agenda(self):
        for contact in self.all_contacts:
            print(contact)


class Friend(Contact):
    def __init__(self, name, email, phone):
        Contact.__init__(self, name, email)
        self.phone = phone

    def __repr__(self):
        # Problema diamant
        return Contact.__repr__(self) + " Phone: " + str(self.phone)


if __name__ == '__main__':
    agenda = Agenda()
    agenda.add_contact(Friend("Alexito", "sorinnoroc@gmail.com", "0730733530"))
    agenda.add_contact(Friend("Valentin", "ampachet@gmail.com", "0730733530"))
    agenda.add_contact(Friend("Marius", "niceman@gmail.com", "0480733530"))
    agenda.print_agenda()
