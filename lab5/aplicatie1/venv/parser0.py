import os
import tkinter as tk
from multiprocessing import Process, Queue
import pygubu


def is_prime(num):
    if num > 1:
        # Iterate from 2 to n // 2
        for i in range(2, (num // 2) + 1):
            # If num is divisible by any number between
            # 2 and n / 2, it is not prime
            if (num % i) == 0:
                return False
        else:
            return True
    else:
        return False


class Parser:
    ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

    def __init__(self, master: tk.Tk):
        self.master = master
        # 1: Create a builder
        self.builder = builder = pygubu.Builder()

        self.queue = Queue()
        self.integer_list = []

        # 2: Load an ui file
        ui_path = os.path.join(self.ROOT_DIR, 'parser0.ui')
        builder.add_from_file(ui_path)

        # 3: Create the widget using a master as parent
        self.parser = builder.get_object('Parser', master)

        self.add_list_btn = self.builder.get_object('add_list_btn')
        self.integer_list_text = self.builder.get_object('integer_list_text')

        self.text_frame = self.builder.get_object('text1')
        self.odd_button = self.builder.get_object('button1')
        self.primes_button = self.builder.get_object('button2')
        self.sum_nums_button = self.builder.get_object('button3')

        self.add_list_btn['command'] = self.add_list
        self.odd_button['command'] = self.odd
        self.primes_button['command'] = self.prime
        self.sum_nums_button['command'] = self.summ

        builder.connect_callbacks(self)
        self.final()

    def prime(self):
        processPrime = Process(target=self.filter_prime, args=(self.queue, self.integer_list))
        processPrime.start()

    def odd(self):
        processOdd = Process(target=self.filter_odd, args=(self.queue, self.integer_list))
        processOdd.start()

    def summ(self):
        processSum = Process(target=self.calculate_sum, args=(self.queue, self.integer_list))
        processSum.start()

    def final(self):
        while not self.queue.empty():
            self.showInGui(str(self.queue.get()))
        self.master.after(50, self.final)

    def showInGui(self, string: str):
        self.text_frame.insert(tk.END, string + "\n")

    def add_list(self):
        result = self.integer_list_text.get("1.0", tk.END)
        result = result.strip().replace(' ', '')
        result = [int(item) for item in result.split(',')]
        self.integer_list = result

    @staticmethod
    def filter_odd(queue: Queue, ls):
        odd = list(filter(lambda x: x % 2 != 0, ls))
        queue.put(odd)

    @staticmethod
    def filter_prime(queue, ls):
        primes = list(filter(is_prime, ls))
        queue.put(primes)

    @staticmethod
    def calculate_sum(queue, ls):
        queue.put(sum(ls))


if __name__ == '__main__':
    root = tk.Tk()
    root.title('List Manipulator')
    app = Parser(root)
    root.mainloop()
