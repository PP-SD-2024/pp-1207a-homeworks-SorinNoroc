import os


class AudioFile:
    def __init__(self, filename):
        if not filename.endswith(self.ext):
            raise Exception("Format nesuportat")
        self.filename = filename


class MP3File(AudioFile):
    ext = "mp3"

    def play(self):
        print("se canta {} un mp3".format(self.filename))

class WavFile(AudioFile):
    ext = "wav"

    def play(self):
        print("se canta {} un wav".format(self.filename))


class OggFile(AudioFile):
    ext = "ogg"

    def play(self):
        print("se canta {} un ogg".format(self.filename))


class FlacFile:
    def __init__(self, filename):
        if not filename.endswith('.flac'):
            raise Exception("Format necunoscut")
        self.filename = filename

    def play(self):
        print("se canta {} un flac".format(self.filename))


class AudioTester:

    @staticmethod
    def readFiles():
        path = ""
        while path != "exit":
            path = input("Introduceti locatia unui fisier audio sau [exit]: ")
            AudioTester.checkFile(path)
        print("Adio")

    @staticmethod
    def checkFile(filename):
        if os.path.exists(filename):
            for audio in [MP3File, WavFile, OggFile, FlacFile]:
                try:
                    audio(filename).play()
                    break
                except Exception as e:
                    pass

    @staticmethod
    def run():
        AudioTester.readFiles()


if __name__ == "__main__":
    AudioTester.run()
