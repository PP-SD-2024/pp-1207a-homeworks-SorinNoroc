import requests as r
import os
from time import time


class Response:
    def get(self, url):
        raise NotImplementedError("Interface method!")


class RealResponse(Response):

    def __init__(self, url=None, timestamp=0.0, response=None):
        self.url = url
        self.timestamp: float = timestamp
        self.response = response

    def get(self, url):
        self.url = url
        self.response = r.get(self.url).status_code
        self.timestamp = time()
        return self

    def __str__(self):
        return ", ".join([self.url, str(self.timestamp), str(self.response)])


class ProxyResponse(Response):

    def __init__(self):
        self.responses = []
        self.realResponse = None
        self.cacheFile = "cache.txt"

    def get(self, url):
        self.load()
        urls = list(map(lambda x: x.url, self.responses))
        index = -1
        urlPresent = url in urls
        if urlPresent:
            index = [i for i, u in enumerate(urls) if u == url][0]
        if not urlPresent:
            self.realResponse = RealResponse()
            self.realResponse.get(url)
            self.responses.append(self.realResponse)
            self.updateCache()
            return self.realResponse
        elif urlPresent and time() - self.responses[index].timestamp > 3600:
            # url present or 1 hour passed
            print("Url with timeout updated!")
            self.responses[index].get(url)
        else:
            print("Url already in cache!")
        self.updateCache()
        return self.responses[index]

    def load(self):
        self.responses = []
        if os.path.exists(self.cacheFile):
            with open(self.cacheFile) as cacheFile:
                cache = cacheFile.read()
                cache = cache.split("\n")
                for line in cache:
                    if line:
                        data = line.split(", ")
                        self.responses.append(RealResponse(data[0], float(data[1]), data[2]))

    def save(self, response):
        with open(self.cacheFile, "a") as cacheFile:
            print(response)
            cacheFile.write(str(response))

    def updateCache(self):
        # erase file contents
        open(self.cacheFile, "w").close()

        with open(self.cacheFile, "w") as cacheFile:
            for resp in self.responses:
                cacheFile.write(str(resp) + "\n")


def main():

    urls = [
        "http://mike.tuiasi.ro/labPP09.pdf",
        "https://open.spotify.com/",
    ]

    newUrls = [
        "https://www.youtube.com/",
        "https://leetcode.com/problemset/",

    ]
    proxy = ProxyResponse()
    for url in urls:
        print(proxy.get(url))

    for url in urls:
        print(proxy.get(url))

    for url in newUrls:
        print(proxy.get(url))


if __name__ == '__main__':
    main()
