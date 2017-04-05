import random as rnd
import pygame
import sys
import numpy as np

pygame.init()
bgcolor = (255, 255, 255)
with open("topper.txt", "r") as f:
	linhas = [linha.replace("\n", "") for linha in f]
dim = int(linhas[0])
surf = pygame.display.set_mode((dim,dim))
surf.fill(bgcolor)

linhas = [list(map(int, x.split())) for x in linhas]


for i in linhas[1:]:
	surf.set_at((i[0],i[1]), (0, 0, 0))
	pygame.display.flip()

pygame.time.delay(50000000)
