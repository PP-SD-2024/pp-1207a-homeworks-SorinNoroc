
class State:
    state_machine = None

    def setStateMachine(self, machine):
        self.state_machine = machine


class CocaCola(State):
    def __init__(self, price):
        self.price = price


class Sprite(State):
    def __init__(self, price):
        self.price = price


class Pepsi(State):
    def __init__(self, price):
        self.price = price


class SelectProduct(State):
    def __init__(self):
        self.state_machine = None

    def choose(self):
        optiune = input("Alegeti cola/sprite/pepsi - 1,2,3: ")
        if optiune in ['1', '2', '3']:
            if optiune == '1':
                self.state_machine.setState(self.state_machine.coca_cola_state)
            elif optiune == '2':
                self.state_machine.setState(self.state_machine.sprite_state)
            else:
                self.state_machine.setState(self.state_machine.pepsi_state)


class Observable:
    def __init__(self):
        self.observers = []

    def attach(self, observer):
        self.observers.append(observer)

    def detach(self, observer):
        self.observers.remove(observer)

    def notifyAll(self, *args):
        for obs in self.observers:
            obs.update(*args)


class SelectProductSTM(Observable):
    def __init__(self, selecter, cola, pepsi, sprite):
        super().__init__()
        self.select_product_state: SelectProduct = selecter

        self.coca_cola_state = cola
        self.coca_cola_state.setStateMachine(self)

        self.pepsi_state = pepsi
        self.pepsi_state.setStateMachine(self)

        self.sprite_state = sprite
        self.sprite_state.setStateMachine(self)

        self.current_state = None

    def choose_another_product(self):
        self.current_state = None
        self.select_product_state.choose()

    def setState(self, state):
        self.current_state = state


class WaitingForClient(State):
    def __init__(self):
        self.state_machine = None

    def client_arrived(self):
        self.state_machine.current_state = self.state_machine.insert_money_state


class InsertMoney(State):
    def __init__(self):
        self.state_machine = None

    def insert(self):
        self.state_machine.current_state = self.state_machine.insert_money_state

    def insert_10cent(self):
        self.insert()
        self.state_machine.money += 0.1

    def insert_50cent(self):
        self.insert()
        self.state_machine.money += 0.5

    def insert_1euro(self):
        self.insert()
        self.state_machine.money += 1

    def insert_5euro(self):
        self.insert()
        self.state_machine.money += 5

    def insert_10euro(self):
        self.insert()
        self.state_machine.money += 10


class TakeMoneySTM(Observable):
    def __init__(self, wait, insert):
        super().__init__()
        self.wait_state = wait
        self.insert_money_state = insert
        self.money = 0

        self.current_state = None

    def add_money(self):
        self.notifyAll(self.money)
        choice = int(input("Introduceti bani: NO - 0, 10 cent - 1, 50 cent - 2, 1 euro - 3, 5 euro - 4, 10 euro - 5: "))
        if choice in [0, 1, 2, 3, 4, 5]:
            self.current_state = self.insert_money_state
            if choice == 0:
                return False
            elif choice == 1:
                self.current_state.insert_10cent()
            elif choice == 2:
                self.current_state.insert_50cent()
            elif choice == 3:
                self.current_state.insert_1euro()
            elif choice == 4:
                self.current_state.insert_5euro()
            else:
                self.current_state.insert_10euro()
        redo = input("Mai introduceti? Yes - 1, No - 2: ")
        if redo == '1':
            return True
        return False

    def update_amount_of_money(self, diff):
        self.money += diff


class DisplayObserver:
    def update(self, money):
        print("Introdus: " + str(money) + " euro")


class ChoiceObserver:
    def update(self):
        print("Luati produsul din cosul de jos!")


class VendingMachineSTM(ChoiceObserver):
    def __init__(self, take_money: TakeMoneySTM, select_product: SelectProductSTM):
        self.take_money_stm = take_money
        self.select_product_stm = select_product

    def proceed_to_checkout(self):
        try:
            selected_product = self.select_product_stm.current_state
            if selected_product is not None and self.take_money_stm.money >= selected_product.price:
                self.select_product_stm.notifyAll()
                self.take_money_stm.update_amount_of_money(-selected_product.price)
                self.select_product_stm.current_state = None
                return True
            else:
                print("Nu ati introdus suficienti bani!")
        except Exception as e:
            print("Ati gresit ceva.. Mai incercati!")
        self.take_money_stm.current_state = self.take_money_stm.insert_money_state
        return False


def main():
    dispObserver = DisplayObserver()
    choiceObserver = ChoiceObserver()
    cola = CocaCola(5.60)
    pepsi = Pepsi(4.90)
    sprite = Sprite(4.60)
    productSelecter = SelectProduct()

    productsSTM = SelectProductSTM(productSelecter, cola, pepsi, sprite)
    productSelecter.setStateMachine(productsSTM)

    waiter = WaitingForClient()
    inserter = InsertMoney()

    moneySTM = TakeMoneySTM(waiter, inserter)
    inserter.setStateMachine(moneySTM)
    waiter.setStateMachine(moneySTM)
    moneySTM.attach(dispObserver)

    machineSTM = VendingMachineSTM(moneySTM, productsSTM)
    machineSTM.select_product_stm.attach(choiceObserver)

    while True:
        productSelecter.choose()  # Prompt the user for drink
        while machineSTM.take_money_stm.add_money():
            # Keep adding money
            pass

        while not machineSTM.proceed_to_checkout():
            machineSTM.take_money_stm.add_money()
        # Rest sau alt produs
        choice = input("Doriti rest sau alt produs? 1 sau 2: ")
        if choice == '1':
            print("Luati restul: ", str(machineSTM.take_money_stm.money))
            machineSTM.take_money_stm.money = 0


if __name__ == '__main__':
    main()
