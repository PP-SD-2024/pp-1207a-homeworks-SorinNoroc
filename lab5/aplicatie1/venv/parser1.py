import os
import tkinter as tk


class Parser:
    ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
    A = None
    B = None

    def __init__(self, gui):
        self.gui = gui
        self.gui.title('Exemplul 1 cu Tkinter')

        self.gui.geometry("600x100")

        self.integer_list_lbl = tk.Label(master=self.gui,
                                         text="List of integers:")
        self.todo0_lbl = tk.Label(master=self.gui,
                                  text="TODO_0: Add a Text field to display the result")
        self.todo1_lbl = tk.Label(master=self.gui,
                                  text="TODO_1: Add a button for each operation")

        self.add_list_btn = tk.Button(master=self.gui,
                                      text="Add list",
                                      command=self.add_list)

        self.integer_list_text = tk.Text(self.gui, width=50, height=1)
        self.integer_list_text.insert(tk.END, str(list(range(1, 16)))[1:-1])

        # alignment on the grid
        self.integer_list_lbl.grid(row=0, column=0)
        self.todo0_lbl.grid(row=1, column=1)
        self.todo1_lbl.grid(row=2, column=1)
        self.integer_list_text.grid(row=0, column=1)
        self.add_list_btn.grid(row=0, column=2)

        self.gui.mainloop()

    def add_list(self):
        result = self.integer_list_text.get("1.0", tk.END)
        result = result.strip().replace(' ', '')
        result = [int(item) for item in result.split(',')]
        self.integer_list = result
        print(result)


if __name__ == '__main__':
    root = tk.Tk()
    root.title('Exemplul 1 cu Tkinter')
    app = Parser(root)
    root.mainloop()
