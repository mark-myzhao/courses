from PIL import Image
from math import floor
import matplotlib.pyplot as plt

import src.ToolKit as Tools


def rgb_aver_equalize_hist(img):
    if img.mode != 'RGB':
        return img
    rgb_pixels = list(img.getdata())
    new_pixels = aver_equalize_hist(rgb_pixels)
    new_img = img.copy()
    new_img.putdata(new_pixels)
    return new_img


def hsi_equalize_hist(img):
    if img.mode != 'RGB':
        return img
    rgb_pixels = list(img.getdata())
    hsi_pixels = []
    i_channel = []
    s = img.size[0] * img.size[1]
    for i in range(s):
        tmp_hsi = Tools.rgb_to_hsi(rgb_pixels[i][0], rgb_pixels[i][1], rgb_pixels[i][2])
        i_channel.append(tmp_hsi[2])
        hsi_pixels.append(tmp_hsi)
    i_channel = equalize_hist(i_channel, img.size)
    new_pixels = []
    for i in range(s):
        tmp_rgb = Tools.hsi_to_rgb(hsi_pixels[i][0], hsi_pixels[i][1], i_channel[i])
        new_pixels.append(tmp_rgb)
    new_img = img.copy()
    new_img.putdata(new_pixels)
    return new_img


# 在RGB三个色彩通道上分别做直方图均衡
def rgb_equalize_hist(img):
    if img.mode != 'RGB':
        return img
    rgb_pixels = list(img.getdata())
    rc = Tools.get_one_channel(rgb_pixels, 'R')
    gc = Tools.get_one_channel(rgb_pixels, 'G')
    bc = Tools.get_one_channel(rgb_pixels, 'B')
    rc = equalize_hist(rc, img.size)
    gc = equalize_hist(gc, img.size)
    bc = equalize_hist(bc, img.size)
    new_pixels = Tools.merge_channel(rc, gc, bc)
    new_img = img.copy()
    new_img.putdata(new_pixels)
    return new_img


# 获取各个颜色通道的直方图并做平均
# 用平均且均衡后的直方图对各个颜色通道进行操作
# 返回均衡化后对像素矩阵
def aver_equalize_hist(rgb_pixels):
    r_pixels = Tools.get_one_channel(rgb_pixels, 'R')
    g_pixels = Tools.get_one_channel(rgb_pixels, 'G')
    b_pixels = Tools.get_one_channel(rgb_pixels, 'B')
    # show_histogram(r_pixels, 'Red Channel  ')
    # show_histogram(g_pixels, 'Green Channel  ')
    # show_histogram(b_pixels, 'Blue Channel  ')
    s = len(r_pixels)
    r_his = get_histogram(r_pixels)
    g_his = get_histogram(g_pixels)
    b_his = get_histogram(b_pixels)
    his = []
    for i in range(len(r_his)):
        his.append(int((r_his[i] + g_his[i] + b_his[i]) / 3))
    # plt.plot(range(256), his)
    # plt.xlabel('intensity')
    # plt.title("Aver  Histogram")
    # plt.show()
    n = [his[0]]
    for i in range(1, 256):  # calculate c.d.f
        n.append(his[i] + n[i - 1])
    arg = 255 / s
    new_pixels = []
    for i in range(s):
        r_pixel = floor(arg * n[r_pixels[i]])
        g_pixel = floor(arg * n[g_pixels[i]])
        b_pixel = floor(arg * n[b_pixels[i]])
        new_pixels.append((r_pixel, g_pixel, b_pixel))
    return new_pixels


#  修改自以前实现的函数
def equalize_hist(pixels, img_size):
    """Histogram Equalization.

    Args:
        pixels: input image matrix in one channel
        img_size: size of image

    Returns:
        output image
    """
    his = get_histogram(pixels)
    n = [his[0]]
    for i in range(1, 256):  # calculate c.d.f
        n.append(his[i] + n[i - 1])
    s = img_size[0] * img_size[1]
    arg = 255 / s
    for i in range(s):
        pixels[i] = floor(arg * n[pixels[i]])
    return pixels


def get_histogram(pixels):
    a = [0 for i in range(256)]
    for pel in pixels:
        a[pel] += 1
    return a


def show_histogram(pixels, mes):
    plt.hist(pixels, bins=256, histtype="step")
    plt.xlabel('intensity')
    plt.title(mes + "Histogram")
    plt.show()
