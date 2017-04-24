from PIL import Image
from math import floor
import matplotlib.pyplot as plt

def equalize_hist(img):
    """Histogram Equalization.

    Args:
        img: input image

    Returns:
        output image
    """
    his = get_histogram(img)
    n = [his[0]]
    for i in range(1, 256):  # calculate c.d.f
        n.append(his[i] + n[i - 1])
    pixels = list(img.getdata())
    arg = 255 / (img.width * img.height)
    for i in range(img.width * img.height):
        pixels[i] = floor(arg * n[pixels[i]])
    new_img = img.copy()
    new_img.putdata(pixels)
    return new_img

def get_histogram(img):
    pixels = img.getdata()
    a = [0 for i in range(256)]
    for pel in pixels:
        a[pel] += 1
    return a

def show_histogram(img):
    plt.hist(img.getdata(), bins=256, histtype="step")
    plt.xlabel('intensity')
    plt.title("Histogram")
    plt.show()
