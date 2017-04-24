# import numpy as np
import HistogramExercise as he
import FilteringExercise as fe
from PIL import Image

print('>> Process started...')
print('>> Opening the origin image...')
image = Image.open('./res/82.png')
print('>> Open successfully.')

# 2.2 Histogram Equalization
print('>> [2.2]Applying with Histogram Equalization...')
new_img = he.equalize_hist(image)
new_new_img = he.equalize_hist(new_img)
new_img.save('./output/HE_Output_1.png')
new_new_img.save('./output/HE_Output_2.png')
# use he.show_histogram(img) to show the histogram of an image if needed
#   for example:
#   he.show_histogram(new_img)

# 2.3 Spatial Filtering
# 2.3.1
print('>> [2.3.1]Applying with Spatial Filtering...')
af3 = [[1 / 9 for i in range(3)] for j in range(3)]
af7 = [[1 / 49 for i in range(7)] for j in range(7)]
af11 = [[1 / 121 for i in range(11)] for j in range(11)]
f = [[1 / 16, 1 / 8, 1 / 16],
     [1 / 8, 1 / 4, 1 / 8],
     [1 / 16, 1 / 8, 1 / 16]]
new_img = fe.filter2d(image, af3)
new_img.save('./output/FE_Output_1_1.png')
new_img = fe.filter2d(image, af7)
new_img.save('./output/FE_Output_1_2.png')
new_img = fe.filter2d(image, af11)
new_img.save('./output/FE_Output_1_3.png')
# 2.3.2
print('>> [2.3.2]Applying with Laplacian Filtering...')
lf = [[1, 1, 1], [1, -8, 1], [1, 1, 1]]
new_img = fe.filter2d(image, lf)
new_img.save('./output/FE_Output_2_1.png')
# 2.3.3
print('>> [2.3.3]Applying with Unsharpmask...')
new_img = fe.unsharpmask(image, 1)
new_img.save('./output/FE_Output_3_1.png')
print('>> Process Completed <<')
