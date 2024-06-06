import subprocess
import os

env = os.environ.copy()
print(os.environ['PATH'])
class FILE:
    KOTLIN = 1
    PYTHON = 2
    BASH = 3
    JAVA = 4
    UNKNOWN = 5


class FileChecker:
    def __init__(self, file_type, successor=None):
        self.successor = successor
        self.file_type = file_type
        self.keywords = []
        self.words = []

    def handle(self, file_content):
        if self.check(file_content) == True:
            return self.file_type
        elif self.successor is not None:
            return self.successor.handle(file_content)
        return FILE.UNKNOWN

    def setSuccessor(self, nextHandler):
        self.successor = nextHandler

    def check(self, file_content):
        words = file_content.split(" ")
        for word in words:
            # Check for whole words
            if word in self.words:
                return True

            # Check for substrings like 'fragments'
            for keyword in self.keywords:
                if keyword in word:
                    return True
        return False


class KotlinChecker(FileChecker):

    def __init__(self, file_type):
        super().__init__(file_type)
        self.words = ["val", "var", "when", "by", "lateinit", "fun"]


class PythonChecker(FileChecker):

    def __init__(self, file_type):
        super().__init__(file_type)
        self.words = ["and", "or", "def", "del", "elif", "lambda", "None", "pass", "raise", "yield"]


class BashChecker(FileChecker):

    def __init__(self, file_type):
        super().__init__(file_type)
        self.words = ["fi", "esac"]
        self.keywords = ["#!", "[["]


class JavaChecker(FileChecker):

    def __init__(self, file_type):
        super().__init__(file_type)
        self.keywords = ["System.out", "instanceof"]
        self.words = ["char", "String", "final", "new"]


class Command:
    def execute(self, file_name):
        raise NotImplementedError("Interface Command can't run methods!")


class ExecutePythonCommand(Command):
    def execute(self, file_name):
        createLangFile(file_name, ".py")
        full_file_name = file_name + ".py"

        t = subprocess.run(["python", full_file_name], capture_output=True, text=True, env=env)
        os.remove(full_file_name)
        return t.stdout


class ExecuteJavaCommand(Command):
    def execute(self, file_name):
        createLangFile(file_name, ".java")
        full_file_name = file_name + ".java"
        # subprocess.check_call(['javac', full_file_name], shell=True, env=env)
        #
        # cmd = ['java', file_name + ".jar"]
        #
        # # run java with input, and capturing output
        # proc = subprocess.run(cmd, check=True, capture_output=True, text=True)

        os.remove(full_file_name)
        # return proc.stdout
        return "Java file ran!"


class ExecuteBashCommand(Command):
    def execute(self, file_name):
        line = subprocess.run(['./' + file_name], capture_output=True, text=True)
        return line.stdout


class ExecuteKotlinCommand(Command):
    def execute(self, file_name):
        full_file_name = file_name + ".kt"
        createLangFile(file_name, ".kt")
        # subprocess.check_call(['kotlinc', full_file_name, "-include-runtime", "-d", file_name + ".jar"], shell=True)
        #
        # # prep for running
        # cmd = ['java', "-jar", file_name + ".jar"]
        #
        # # run java with input, and capturing output
        # proc = subprocess.run(cmd, check=True, shell=True, capture_output=True, text=True)
        os.remove(full_file_name)
        # return proc.stdout
        return "Kotlin file ran!"


def createLangFile(file_name, extension):
    with open(file_name) as file:
        content = file.read()

    with open(file_name + extension, "w") as exe:
        exe.write(content)


class ProgramExecuter:

    def __init__(self):
        self.command = None

    def set_command(self, command):
        self.command = command

    def execute_command(self, file_name):
        return self.command.execute(file_name)


def main():
    while True:
        file_name = input("Introduceti numele fisierului(din cwd) sau [exit]: ")
        if "exit" in file_name:
            return 0
        with open(file_name) as file:
            file_content = file.read()

        kotlinHandler = KotlinChecker(FILE.KOTLIN)
        pythonHandler = PythonChecker(FILE.PYTHON)
        bashHandler = BashChecker(FILE.BASH)
        javaHandler = JavaChecker(FILE.JAVA)

        kotlinHandler.setSuccessor(pythonHandler)
        pythonHandler.setSuccessor(bashHandler)
        bashHandler.setSuccessor(javaHandler)

        fileType = kotlinHandler.handle(file_content)

        executer = ProgramExecuter()
        kotlinCommander = ExecuteKotlinCommand()
        pythonCommander = ExecutePythonCommand()
        javaCommander = ExecuteJavaCommand()
        bashCommander = ExecuteBashCommand()

        if fileType == FILE.KOTLIN:
            executer.set_command(kotlinCommander)
        elif fileType == FILE.PYTHON:
            executer.set_command(pythonCommander)
        elif fileType == FILE.BASH:
            executer.set_command(bashCommander)
        elif fileType == FILE.JAVA:
            executer.set_command(javaCommander)
        else:
            print("Couldn't decide file type! Aborting...")
            continue
        output = executer.execute_command(file_name)
        print(output)


if __name__ == "__main__":
    main()
