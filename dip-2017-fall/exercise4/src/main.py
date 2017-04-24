from PIL import Image
import random

import src.MeanFilters as Filter
import src.NoiseGenerator as Ng
import src.Histogram as His
import src.ToolKit as Tools


def grey_to_rgba(grey_img):
    res_img = Image.new('RGBA', grey_img.size)
    data = list(grey_img.getdata())
    for i in range(len(data)):
        tmp = data[i]
        data[i] = (tmp, tmp, tmp, 255)
    res_img.putdata(data)
    return res_img


def exercise2_2():
    img = Image.open('../input/task_1.png')
    # new_img = Filter.mean_filter(img, 3, 0)
    # new_img.save('../output/exercise2_2_1_1.png')
    # new_img = Filter.mean_filter(img, 9, 0)
    # new_img.save('../output/exercise2_2_1_2.png')
    # new_img = Filter.mean_filter(img, 3, 1)
    # new_img.save('../output/exercise2_2_2_1.png')
    # new_img = Filter.mean_filter(img, 9, 1)
    # new_img.save('../output/exercise2_2_2_2.png')
    # new_img = Filter.mean_filter(img, 3, 2)
    # new_img.save('../output/exercise2_2_3_1.png')
    # new_img = Filter.mean_filter(img, 9, 2)
    # new_img.save('../output/exercise2_2_3_2.png')


def exercise2_3():
    img = Image.open('../input/task_2.png')
    gene = Ng.NoiseGenerator()

    # 添加N(0, 40)高斯噪声
    new_img = gene.gaussian_generator(img, 0, 40)
    new_img.save('../output/exercise2_3_2_addGaussNoise.png')
    data = list(new_img.getdata())
    for i in range(len(data)):
        data[i] = data[i][0]
    grey_img = Image.new('L', new_img.size)
    grey_img.putdata(data)
    # 算数均值滤波
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.ARITHMETIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_2_AMF.png')
    # 几何均值滤波
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.GEOMETRIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_2_GMF.png')
    # 中值滤波
    filtered_img = Filter.median_filter(grey_img, 3)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_2_MF.png')

    # 20%盐噪声
    new_img = gene.salt_and_pepper_generator(img, 0.2, 0)
    new_img.save('../output/exercise2_3_2.png')
    data = list(new_img.getdata())
    for i in range(len(data)):
        data[i] = data[i][0]
    grey_img = Image.new('L', new_img.size)
    grey_img.putdata(data)
    # 最小值滤波
    filtered_img = Filter.min_filter(grey_img, 3)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_3_MinF.png')
    # 调和滤波
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.HARMONIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_3_HMF.png')
    # 反调和滤波 q = 1.5
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.HARMONIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_3_CHMF_PosQ.png')
    # 反调和滤波 q = -1.5
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.CONTRAHARMONIC_FILTER, -1.5)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_3_CHMF_NegQ.png')

    # # 20%椒盐噪声
    new_img = gene.salt_and_pepper_generator(img, 0.2, 0.2)
    new_img.save('../output/exercise2_3_3.png')
    data = list(new_img.getdata())
    for i in range(len(data)):
        data[i] = data[i][0]
    grey_img = Image.new('L', new_img.size)
    grey_img.putdata(data)
    # 算数均值
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.ARITHMETIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_4_AMF.png')
    # 几何均值
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.GEOMETRIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_4_GMF.png')
    # 调和滤波
    filtered_img = Filter.mean_filter(grey_img, 3, Filter.HARMONIC_FILTER)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_4_HMF.png')
    # 中值滤波
    filtered_img = Filter.median_filter(grey_img, 3)
    grey_to_rgba(filtered_img).save('../output/exercise2_3_4_MF.png')


def exercise2_4():
    img = Image.open('../input/82.png')
    # r, g, b通道分布直方图均衡化再组合
    new_img = His.rgb_equalize_hist(img)
    new_img.save('../output/ex2_4/1.png')
    # 平均直方图进行均衡化操作
    new_img = His.rgb_aver_equalize_hist(img)
    new_img.save('../output/ex2_4/2.png')
    # hsi模式i通道直方图均衡化、
    new_img = His.hsi_equalize_hist(img)
    new_img.save('../output/ex2_4/3.png')

# exercise2_2()
# exercise2_3()
exercise2_4()
