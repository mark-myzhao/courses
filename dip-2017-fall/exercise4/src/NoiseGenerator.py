import math
import random


class NoiseGenerator(object):
    def __init__(self):
        self.__phase = True
        self.__z0 = 0
        self.__z1 = 0
        self.__MIN_VALUE = 0.00000001

    def gaussian_generator(self, img, mu, sigma):
        data = list(img.getdata())
        for i in range(len(data)):
            tmp = data[i][0] + self.gaussian_func(mu, sigma)
            tmp = 0 if tmp < 0 else int(tmp)
            data[i] = (tmp, tmp, tmp, data[i][3])
        new_img = img.copy()
        new_img.putdata(data)
        return new_img

    # Box-Muller
    def gaussian_func(self, mu, sigma):
        # v1 = random.uniform(0, 1)
        # v2 = random.uniform(0, 1)
        self.__phase = not self.__phase
        if self.__phase:
            return self.__z1 * sigma + mu

        u1 = random.uniform(0, 1)
        u2 = random.uniform(0, 1)
        while u1 < self.__MIN_VALUE:
            u1 = random.uniform(0, 1)
            u2 = random.uniform(0, 1)
        self.__z0 = math.sqrt(-2.0 * math.log(u1)) * math.cos(2 * math.pi * u2)
        self.__z1 = math.sqrt(-2.0 * math.log(u1)) * math.sin(2 * math.pi * u2)
        return self.__z0 * sigma + mu

    def salt_and_pepper_generator(self, img, salt_prob, pepper_prob):
        data = list(img.getdata())
        for i in range(len(data)):
            pixel_value = data[i][0]
            flag_salt = random.uniform(0, 1)
            flag_pepper = random.uniform(0, 1)
            if (flag_pepper < pepper_prob) and (flag_salt < salt_prob):
                flag = random.uniform(0, 1)
                pixel_value = 0 if flag < 0.5 else 255
            elif flag_salt < salt_prob:
                pixel_value = 255
            elif flag_pepper < pepper_prob:
                pixel_value = 0
            data[i] = (pixel_value, pixel_value, pixel_value, data[i][3])
        new_img = img.copy()
        new_img.putdata(data)
        return new_img
