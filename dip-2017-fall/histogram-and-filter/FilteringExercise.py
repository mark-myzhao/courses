from math import floor


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

def filter2d(input_img, f):
    """Filtering, use correlation method.

    Args:
        input_img: Input image
        f:   A filter, a 2-D n*n array

    Returns:
        the new image after applied with filter
    """
    mat = listtomatrix(list(input_img.getdata()), input_img.width, input_img.height)
    s = len(f)
    pos_mat = padding(mat, s - 1)
    res_mat = []
    for each_row in pos_mat:
        res_mat.append(each_row.copy())
    m, n = len(mat), len(mat[0])
    for cur_x in range(s - 1, s - 1 + m):
        for cur_y in range(s - 1, s - 1 + n):
            res = 0
            i, j = 0, 0
            start_x, start_y = cur_x - (floor((s+1)/2) - 1), cur_y - (floor((s+1)/2) - 1)
            for i in range(s):
                for j in range(s):
                    res = res + f[i][j] * pos_mat[start_x+i][start_y+j]
            res_mat[cur_x][cur_y] = res
    new_img = input_img.copy()
    new_img.putdata(matrixtolist(chop(res_mat, s - 1)))
    return new_img


def unsharpmask(img, k):
    af = [[1 / 9 for af_i in range(3)] for af_j in range(3)]
    tmp_img = filter2d(img, af)
    img_data = img.getdata()
    tmp_data = tmp_img.getdata()
    new_pixels = []
    for i in range(img.width * img.height):
        tmp = img_data[i] + k * (img_data[i] - tmp_data[i])
        if tmp < 0:
            tmp = 0
        elif tmp > 255:
            tmp = 255
        new_pixels.append(tmp)
    new_img = img.copy()
    new_img.putdata(new_pixels)
    return new_img

def listtomatrix(l, w, h):
    res = []
    for i in range(h):
        res.append(l[i * w:i * w + w])
    return res


def matrixtolist(mat):
    res = []
    for row in mat:
        for ele in row:
            res.append(ele)
    return res
