import re


def readFile(file: str):
    if file.endswith(".txt"):
        with (open(file) as txtFile):
            return txtFile.read()


def eliminatePunctuation(text: str):
    punctuation = ".,!:;\"\'?"
    for char in punctuation:
        text = text.replace(char, "")
    return text


def convertToLower(text: str):
    return text.lower()


def replaceMultipleSpaces(text):
    return re.sub(" {2,}", " ", text)


def main():
    fileText = readFile("text.txt")
    print("Continutul dupa prelucrare: ", replaceMultipleSpaces(convertToLower(eliminatePunctuation(fileText))))


if __name__ == '__main__':
    main()
