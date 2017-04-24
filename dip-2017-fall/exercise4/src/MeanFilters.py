from math import floor
import sys


ARITHMETIC_FILTER = 0
HARMONIC_FILTER = 1
CONTRAHARMONIC_FILTER = 2
GEOMETRIC_FILTER = 3
# arithmetic, 1 for harmonic, 2 for contra harmonic, 3 for geometric


def padding(mat, padding_num):
    new_mat = []
    m, n = len(mat), len(mat[0])
    for i in range(padding_num * 2 + m):
        new_mat.append([0 for j in range(padding_num * 2 + n)])
    # start at (padding_num, padding_num)
    cur_x, cur_y = padding_num, padding_num
    for row in mat:
        for ele in row:
            new_mat[cur_x][cur_y] = ele
            cur_y += 1
        cur_y = padding_num
        cur_x += 1
    return new_mat


def chop(mat, padding_num):
    new_mat = []
    m, n = len(mat), len(mat[0])
    for i in range(padding_num, m - padding_num):
        new_mat.append(mat[i][padding_num:(n - padding_num)])
    return new_mat


def mean_filter(input_img, s, flag, q=1.5):
    """Filtering, use correlation method.

    Args:
        input_img: Input image
        s: the size of filter
        flag: 0 for arithmetic, 1 for harmonic, 2 for contra harmonic, 3 for geometric

    Returns:
        the new image after applied with filter
    """
    mat = list_to_matrix(list(input_img.getdata()), input_img.width, input_img.height)
    pos_mat = padding(mat, s - 1)
    res_mat = []
    for each_row in pos_mat:
        res_mat.append(each_row.copy())
    m, n = len(mat), len(mat[0])

    for cur_x in range(s - 1, s - 1 + m):
        for cur_y in range(s - 1, s - 1 + n):
            res_mat[cur_x][cur_y] = mean_calculator(cur_x, cur_y, s, pos_mat, flag, q)

    new_img = input_img.copy()
    new_img.putdata(matrix_to_list(chop(res_mat, s - 1)))
    return new_img


def min_filter(input_img, s):
    mat = list_to_matrix(list(input_img.getdata()), input_img.width, input_img.height)
    pos_mat = padding(mat, s - 1)
    res_mat = []
    for each_row in pos_mat:
        res_mat.append(each_row.copy())
    m, n = len(mat), len(mat[0])

    for cur_x in range(s - 1, s - 1 + m):
        for cur_y in range(s - 1, s - 1 + n):
            res = 255
            start_x, start_y = cur_x - (floor((s + 1) / 2) - 1), cur_y - (floor((s + 1) / 2) - 1)
            for i in range(s):
                for j in range(s):
                    tmp = pos_mat[start_x + i][start_y + j]
                    res = tmp if tmp < res else res
            res_mat[cur_x][cur_y] = res
    new_img = input_img.copy()
    new_img.putdata(matrix_to_list(chop(res_mat, s - 1)))
    return new_img


def median_filter(input_img, s):
    mat = list_to_matrix(list(input_img.getdata()), input_img.width, input_img.height)
    pos_mat = padding(mat, s - 1)
    res_mat = []
    for each_row in pos_mat:
        res_mat.append(each_row.copy())
    m, n = len(mat), len(mat[0])

    for cur_x in range(s - 1, s - 1 + m):
        for cur_y in range(s - 1, s - 1 + n):
            res = []
            start_x, start_y = cur_x - (floor((s + 1) / 2) - 1), cur_y - (floor((s + 1) / 2) - 1)
            for i in range(s):
                for j in range(s):
                    res.append(pos_mat[start_x + i][start_y + j])
            res.sort()
            res_mat[cur_x][cur_y] = res[int(s * s / 2)]
    new_img = input_img.copy()
    new_img.putdata(matrix_to_list(chop(res_mat, s - 1)))
    return new_img


def mean_calculator(cur_x, cur_y, s, pos_mat, flag, q=1.5):
    res = 1 if flag == GEOMETRIC_FILTER else 0
    sum_r = 0
    start_x, start_y = cur_x - (floor((s + 1) / 2) - 1), cur_y - (floor((s + 1) / 2) - 1)
    for i in range(s):
        for j in range(s):
            if flag == 0:
                res = res + pos_mat[start_x + i][start_y + j]
            elif flag == 1:
                if pos_mat[start_x + i][start_y + j] == 0:
                    res = sys.maxsize
                else:
                    res += 1 / pos_mat[start_x + i][start_y + j]
            elif flag == 2:
                tmp = pos_mat[start_x + i][start_y + j]
                res = res + (tmp ** q) if tmp != 0 else 0
                sum_r += (tmp ** (q + 1)) if tmp != 0 else 0
            elif flag == 3:  # 几何均值
                res *= pos_mat[start_x + i][start_y + j]
    if flag == 2:
        if res == 0:
            return 0 if q >= 0 else 255
        else:
            return sum_r / res
    else:
        return mean_get_res(res, s, flag)


def rgba_mean_calculator(cur_x, cur_y, s, pos_mat, flag, q=1.5):
    res = 1 if flag == GEOMETRIC_FILTER else 0
    sum_r = 0
    start_x, start_y = cur_x - (floor((s + 1) / 2) - 1), cur_y - (floor((s + 1) / 2) - 1)
    for i in range(s):
        for j in range(s):
            if flag == 0:
                res = res + pos_mat[start_x + i][start_y + j][0]
            elif flag == 1:
                if pos_mat[start_x + i][start_y + j][0] == 0:
                    res = sys.maxsize
                else:
                    res += 1 / pos_mat[start_x + i][start_y + j][0]
            elif flag == 2:
                res += (pos_mat[start_x + i][start_y + j][0] ** q)
                sum_r += (pos_mat[start_x + i][start_y + j][0] ** (q + 1))
            elif flag == 3:  # 几何均值
                res *= pos_mat[start_x + i][start_y + j][0]
    if flag == 2:
        if res == 0:
            return 0 if q >= 0 else 255
        else:
            return sum_r / res
    else:
        return rgba_mean_get_res(res, s, flag)


def mean_get_res(res, s, flag):
    if flag == 0:
        return res / (s * s)
    elif flag == 1:
        return (s * s) / res
    elif flag == 3:
        tmp = res ** (1 / (s * s))
        return tmp


def rgba_mean_get_res(res, s, flag):
    if flag == 0:
        tmp = res / (s * s)
        return tmp, tmp, tmp, 255
    elif flag == 1:
        tmp = (s * s) / res
        return tmp, tmp, tmp, 255
    elif flag == 3:
        tmp = res ** (1 / (s * s))
        return tmp, tmp, tmp, 255


def list_to_matrix(l, w, h):
    res = []
    for i in range(h):
        res.append(l[i * w:i * w + w])
    return res


def matrix_to_list(mat):
    res = []
    for row in mat:
        for ele in row:
            res.append(ele)
    return res
