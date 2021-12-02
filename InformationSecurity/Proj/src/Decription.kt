
fun main(){
    //암호문, 키 입력받기
    print("암호문을 입력하세요 : ")
    val password = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()

    if (key == null || password == null) {
        println("평문, 키를 입력하세요")
        return
    }

    //암호문 이진화
    var binaryPassword = ""
    for (i in password.indices) {
        var tmpPass: String = Integer.toBinaryString(password[i]-'0')
        while (tmpPass.length < 2) {
            tmpPass = ("0$tmpPass")
        }
        binaryPassword += tmpPass
    }
    println("binaryP: $binaryPassword")

    //키 아스키코드
    var codeKey = ""
    for (i in key.indices) {
        codeKey += key[i].code
    }

    //아스키코드 키 이진화, 길이 늘려서 암호문과 같게
    var binaryKey = ""
    var tmpBinaryKey = ""
    for (i in codeKey.indices) {
        binaryKey = Integer.toBinaryString(codeKey[i]-'0')
        while(binaryKey.length < 4) {
            binaryKey = "0$binaryKey"
        }
        tmpBinaryKey += binaryKey
    }
    binaryKey = ""
    var tmpStart = 0
    for (i in binaryPassword.indices) {
        binaryKey += tmpBinaryKey[tmpStart]
        if (tmpStart < tmpBinaryKey.length-1) {
            tmpStart ++
        } else {
            tmpStart = 0
        }
    }
    println("binaryK: $binaryKey")
    //키 평문 xor
    var xorPass = ""
    for (i in binaryPassword.indices) {
        xorPass += if (binaryPassword[i] == binaryKey[i]) {
            0
        } else {
            1
        }
    }
    println("xorPass: $xorPass")

    var codePass = ""
    for (i in xorPass.indices step 4) {
        val tmp = ((xorPass[i]-'0') * 8 + (xorPass[i+1]-'0') * 4 + (xorPass[i+2]-'0') * 2 + (xorPass[i+3]-'0'))
        codePass += tmp
    }
    println("아스키 코드: $codePass")
    var strPass = ""
    for (i in codePass.indices step 2) {
        val tmp: Char = (((codePass[i]-'0') * 10 + (codePass[i+1]-'0')).toChar())
        strPass += tmp
    }
    println("전치된 평문: $strPass")

    //키 정렬
    val keySort = mutableListOf<Char>()
    for (i in key.indices) {
        keySort.add(key[i])
    }
    keySort.sort()
    println("정렬된 키: $keySort")
    //a e l p p
    //a p p l e
    //1 4 5 3 2

    val keySequence: MutableList<Int> = emptyList<Int>().toMutableList()
    for (i in key.indices) {
        for (j in keySort.indices) {
            if (key[i] == keySort[j]) {
                keySequence.add(j)
                keySort[j]='_'
            }
        }
    }
    //키 순서?
    println("키 순서: $keySequence")


    var startPoint = 0
    var endPoint = key.length-1
    var tmpString = ""
    var sortPass = ""
    val keyDev = if (strPass.length % key.length == 0) {
        strPass.length / key.length
    } else {
        strPass.length / key.length + 1
    }

    for (i in 0 until keyDev) {//전치해서 추가 키 자리수로 나누어 떨어지는 부분들에 대해서
        if (endPoint < strPass.length) {
            tmpString = strPass.substring(startPoint, endPoint+1)
            //21403
            //BCUGX XJI
            //GCBXU JXI

            val tmpCharArray = arrayOfNulls<Char>(tmpString.length)
            for (j in tmpString.indices) {
                for (k in keySequence.indices) {
                    if (keySequence[k] == j) {
                        tmpCharArray[k]=tmpString[j]
                        break
                    }
                }
            }
            for (j in tmpCharArray.indices) {
                sortPass += tmpCharArray[j]
            }
        } else {//마지막부분 키 남은만큼 짤라서
            tmpString = strPass.substring(startPoint)
            val tmpKey = key.substring(0, strPass.length % key.length)
            val tmpKeySort: MutableList<Char> = emptyList<Char>().toMutableList()
            for (i in tmpKey.indices) {
                tmpKeySort.add(tmpKey[i])
            }
            tmpKeySort.sort()
            //다시 정렬하고
            val tmpKeySequence: MutableList<Int> = emptyList<Int>().toMutableList()
            for (i in tmpKey.indices) {
                for (j in tmpKeySort.indices) {
                    if (tmpKey[i] == tmpKeySort[j]) {
                        tmpKeySequence.add(j)
                        tmpKeySort[j]='_'
                    }
                }
            }
            //정렬한거 순서 찾아서
            val tmpCharArray = arrayOfNulls<Char>(tmpString.length)
            for (j in tmpString.indices) {
                for (k in tmpKeySequence.indices) {
                    if (tmpKeySequence[k] == j) {
                        tmpCharArray[k]=tmpString[j]
                        break
                    }
                }
            }
            for (j in tmpCharArray.indices) {
                sortPass += tmpCharArray[j]
            }
            //적용하기
        }
        startPoint += key.length
        endPoint += key.length
    }
    println("시프트된 평문: $sortPass")

    var planeText = ""
    for (i in sortPass.indices) {
        planeText += if (sortPass[i] - (key[i % key.length].code) % 26 !in 'A'..'Z') {
            sortPass[i] - (key[i % key.length].code) % 26 + 26
        } else {
            sortPass[i] - (key[i % key.length].code) % 26
        }
    }
    println("평문: $planeText")
}