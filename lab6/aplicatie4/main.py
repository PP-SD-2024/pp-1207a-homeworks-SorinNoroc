muschi = []
trunchiuri = []


class Celula:
    def get_nume(self) -> str:
        raise NotImplementedError("Not implemented!")


class FibraMusculara(Celula):

    def __init__(self, nume, mm):
        self.nume = nume
        self.masa_musculara = mm

    def get_nume(self):
        return self.nume

    def get_masa_musculara(self):
        return self.masa_musculara


class FibraNervoasa(Celula):

    def __init__(self, nume, lungime):
        self.nume = nume
        self.lungime = lungime

    def get_nume(self) -> str:
        return self.nume

    def get_lungime(self):
        return self.lungime


class MuschiGeneric(FibraMusculara):

    def __init__(self, nume, mm, scop):
        super().__init__(nume, mm)
        self.scop = scop
        self.fibre = []
        muschi.append(self)

    def __repr__(self):
        return "Muschi cu nume: {}, masa_musculara: {}, scop: {}\n".format(self.nume, self.masa_musculara, self.scop)

    def get_scop(self):
        return self.scop


class TrunchiNervos(FibraNervoasa):

    def __init__(self, nume, lungime, specializare):
        super().__init__(nume, lungime)
        self.specializare = specializare
        trunchiuri.append(self)

    def __repr__(self):
        return "Trunchi Nervos cu nume: {}, lungime: {}, specializare: {}\n".format(self.nume, self.lungime, self.specializare)

    def get_specializare(self):
        return self.specializare


if __name__ == "__main__":
    # A
    bicepsStang = MuschiGeneric("biceps", 20, {"locomotor", "incordare brat stang"})
    bicepsDrept = MuschiGeneric("biceps", 15, {"locomotor", "incordare brat drept"})
    triceptStang = MuschiGeneric("triceps", 20, {"locomotor", "relaxare brat stang"})
    triceptDrept = MuschiGeneric("triceps", 15, {"locomotor", "relaxare brat drept"})
    gambaStanga = MuschiGeneric("gamba", 30, {"locomotor", "incordare picior stang"})
    trunchiCervical = TrunchiNervos("cervical", 10, {"coloana", "control membre superioare"})
    trunchiLombar = TrunchiNervos("lombar", 10, {"coloana", "control membre inferioare"})
    # B
    print("Suma muschilor: ", sum(list(map(lambda x: x.get_masa_musculara(), muschi))))
    print("Suma trunchiurilor: ", sum(list(map(lambda x: x.get_lungime(), trunchiuri))))
    # C
    print("Muschi locomotori: ", list(filter(lambda x: "locomotor" in x.get_scop(), muschi)))
    print("Trunchiuri ale coloanei: ", list(filter(lambda x: "coloana" in x.get_specializare(), trunchiuri)))
