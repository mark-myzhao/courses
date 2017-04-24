import math


# return (h, s, i)
def rgb_to_hsi(r, g, b):
    tmp = 0.5 * ((r - g) + (r - b))
    flag = (r == b) and (b == g)
    tmp = 1 if flag else tmp / math.sqrt(((r - g)**2 + (r - b) * (g - b)))
    angle = (math.acos(tmp) * 180.0) / math.pi
    h = angle if b <= g else 360 - angle
    tmp = r + g + b
    s = 0 if tmp == 0 else 1 - (3 / tmp) * min((r, g, b))
    i = tmp / 3
    return h, s, int(i)


# return (r, g, b)
def hsi_to_rgb(h, s, i):
    r, g, b = 0, 0, 0
    x = i * (1 - s)
    if 0 <= h < 120:
        b = x
        tmp = s * math.cos(degree_to_radian(h)) / math.cos(degree_to_radian(60 - h))
        r = i * (1 + tmp)
        g = 3 * i - (r + b)
    elif 120 <= h < 240:
        r = x
        h -= 120
        tmp = s * math.cos(degree_to_radian(h)) / math.cos(degree_to_radian(60 - h))
        g = i * (1 + tmp)
        b = 3 * i - (r + g)
    elif 240 <= h < 360:
        g = x
        h -= 240
        tmp = s * math.cos(degree_to_radian(h)) / math.cos(degree_to_radian(60 - h))
        b = i * (1 + tmp)
        r = 3 * i - (g + b)
        pass
    return int(r), int(g), int(b)


def degree_to_radian(degree):
    radian = degree * math.pi / 180
    return radian


def merge_channel(rc, gc, bc):
    merged_pixels = []
    for i in range(len(rc)):
        merged_pixels.append((rc[i], gc[i], bc[i]))
    return merged_pixels


def get_one_channel(rgb_pixels, channel):
    d = {'R': 0, 'G': 1, 'B': 2}
    if channel in d:
        select = d[channel]
    else:  # invalid args
        return rgb_pixels
    oc_pixels = []
    for ele in rgb_pixels:
        oc_pixels.append(ele[select])
    return oc_pixels
