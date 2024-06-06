import os
from collections import Counter

big = [[9, 10, 13], range(32, 128)]
small = [range(9), [11, 12, 14], range(15, 31), range(128, 256)]


class GenericFile:
    def get_path(self):
        raise NotImplementedError("Not implemented yet! Implement it first!")

    def get_freq(self):
        raise NotImplementedError("Not implemented yet! Implement it please!")


class TextASCII(GenericFile):
    def __init__(self, path):
        self.path_absolut = path
        self.frecvente = dict()

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

    def check_freq(self):
        with open(self.path_absolut) as file:
            text = file.read()
        self.text = text
        c = dict(Counter(text))
        ordDict = dict()
        for key, val in c.items():
            ordDict[ord(key)] = val

        sortDict = list(sorted(ordDict.items(), key=lambda x: x[1]))
        self.frecvente = sortDict

    def isItAscii(self):
        few_count = 0
        for i in range(int(len(self.frecvente) / 2)):
            for ls in small:
                frecv = self.frecvente[i]
                if frecv[0] in ls:
                    few_count += frecv[1]

        many_count = 0
        for i in range(int(len(self.frecvente) / 2), len(self.frecvente)):
            for ls in big:
                frecv = self.frecvente[i]
                if frecv[0] in ls:
                    many_count += frecv[1]

        if many_count > few_count * 10:
            return f"\"{self.path_absolut}\" " + "is Ascii!"
        return f"\"{self.path_absolut}\" " + "is Ascii!"


class TextUnicode(GenericFile):
    def __init__(self, path):
        self.path_absolut = path
        self.freq = self.check_freq()

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.freq

    def check_freq(self):
        with open(self.path_absolut) as file:
            text = file.read()
        c = dict(Counter(text))

        if '0' in c.keys() and c['0'] / len(text) > 0.3:
            return f"\"{self.path_absolut}\" " + "is Unicode!"
        return f"\"{self.path_absolut}\" " + "is not Unicode!"


if __name__ == "__main__":
    ascii = TextASCII("/home/student/Downloads/aplicatia-1.kt")
    ascii.check_freq()
    print(ascii.isItAscii())

    unicode = TextUnicode("utf16")
    print(unicode.check_freq())

    unicode = TextUnicode("/home/student/Downloads/aplicatia-1.kt")
    print(unicode.check_freq())
