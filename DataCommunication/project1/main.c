#include <stdio.h>
#include <stdlib.h>

int main() {
	
	//input.txt 를 읽을 배열  
	char input[999999] = {NULL, };	
	//crc16 = 1 1000 0000 0000 0101	
	int crc_16 = 98309;
	//결과값 저장, 연산을 위한 변수  
	unsigned int crc_result = 0;
	int crc_result_binary[16] = {0,};
	int shift = 16;
	
	//입력을 이진수로 바꾸기 위해 필요한 변수 
	int binary[8];
	int input_size = 0;
	int position = 1;
	int one_count = 0;	
	
	//파일 읽기 
	FILE *fp = fopen("input.txt","r");
	fread(input, sizeof(input) - 1, 1, fp);

	//입력 사이즈  
	for (int i = 0; i < sizeof(input)/sizeof(char); i++) {	
		if (input[i] == NULL) {
			break;
		}
		input_size ++;
	}
	
	//입력받은 문자열을 변환해서 저장할 2진수 배열  
	char* arr = (char*)malloc(sizeof(char) * ((input_size+2)*8));
	int arr_size = (input_size+2)*8;
	memset(arr, 0, sizeof(char) * arr_size);
	
	//읽어온 텍스트의 각 글자마다  
	for (int i = 0; i < sizeof(input)/sizeof(char); i++) {	
		if (input[i] == NULL) {
			break;
		}
		
		int tmp = input[i];	//이진수로  변환  
		while (1){
			binary[position] = tmp % 2;
			if(tmp%2 == 1) one_count++;
			tmp = tmp / 2;
			position ++;
			if (tmp == 0) break;
		}
		//even parity bit 추가  
		if (one_count % 2 == 1) {
			binary[0] = 1;
		} else {
			binary[0] = 0;
		}
		//binary 배열 역순으로 읽고 arr에 추가하기   
		for(int j = position-1; j >= 0; j--) {
			arr[j + ((input_size + 1) * 8)] = binary[j];
		}
		input_size --; 
		one_count = 0;
		position = 1;
	}
	
	for (int i = arr_size - 1; i >= 0; ) {
		while(shift >= 0) {
			crc_result += (arr[i] << shift);
			i --;
			shift --; 
		}
		crc_result = crc_16 ^ crc_result; 
		
		if (i==-1) {	//계산 완료 
		 	//이진수로  변환  
			position = 0;
			int tmp = crc_result;
			while (1){
				crc_result_binary[position] = tmp % 2;
				tmp = tmp / 2;
				position ++;
				if (tmp == 0) break;
			}
			//변환한 수 읽 
			for(int k = 15; k >= 0; k--) {
				printf("%d", crc_result_binary[k]);
				if (k % 4 == 0) printf(" ");
			}
		
			break;
		}
		for (int j = 0; j < 17; j ++) {
			if ((crc_result & (unsigned int)0x10000) == 0){
				crc_result = crc_result << 1;
				shift ++;
			} else {
				break;
			}
		}
	}
		
	fclose(fp);
	free(arr); 
	return 0;
}
