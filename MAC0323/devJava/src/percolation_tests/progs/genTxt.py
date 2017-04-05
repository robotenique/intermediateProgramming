from PIL import Image as im
import numpy as np
img = im.open("emojo.png")
tarr = np.asarray(img.convert('L'))
points = list("100")
for i in range(tarr.shape[0]):
	for j in range(tarr.shape[0]):
		if tarr[i][j] >= 230:
			points.append( f"{i} {j}\n")
for p in points:
	print(p, end="")