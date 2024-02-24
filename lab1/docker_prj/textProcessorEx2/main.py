def readFile(file: str):
    if file.endswith(".txt"):
        with (open(file) as txtFile):
            return txtFile.read()


def eliminatePunctuation(text: str):
    punctuation = ".,!:;\"\'?"
    for char in punctuation:
        text = text.replace(char, "")
    return text


def main():
    print("File contents without punctuation: ", eliminatePunctuation(readFile("text.txt")))


if __name__ == '__main__':
    main()
