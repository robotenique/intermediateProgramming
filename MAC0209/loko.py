def canBStacked (k):
    return not any((badCase(k[i], k[i+1], k[i+2]) for i in range(len(k) - 2)))

def badCase(a, b, c):
    print(f"check = {a},{b},{c}")
  	# Altas coisas inútil
    values = [a > b, a > c, b < a, b < c, c > b, c < a]
    return all(values)

def main():
    k = list(map(int, input("Sequência: ").split(" ")))
    print(canBStacked(k))


if __name__ == '__main__':
    main()
