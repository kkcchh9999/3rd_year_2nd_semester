#include <stdio.h>
#include <stdlib.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(int argc, char *argv[]) {
	
	char input[999999] = {0, };
	printf("�Է� : ");
	scanf("%s", &input);
		
	//�Է��� �������� �ٲٱ� ���� �ʿ��� ���� 
	int binary[8];
	int input_size = 0;
	int position = 1;
	int one_count = 0;	
	
	//�Է� ������  
	for (int i = 0; i < sizeof(input)/sizeof(char); i++) {	
		if (input[i] == NULL) {
			break;
		}
		input_size ++;
	}
	int flag = input_size % 2;
	
	char *arr;
	int arr_size = 0;
	//�Է¹��� ���ڿ��� ��ȯ�ؼ� ������ 2���� �迭  
	if (flag == 0) {
		arr = (char*)malloc(sizeof(char) * ((input_size)*8));
		arr_size = (input_size)*8;
		memset(arr, 0, sizeof(char) * arr_size);
	} else {
		arr = (char*)malloc(sizeof(char) * ((input_size+1)*8));
		arr_size = (input_size+1)*8;
		memset(arr, 0, sizeof(char) * arr_size);
	}
	//�о�� �ؽ�Ʈ�� �� ���ڸ���  
	for (int i = 0; i < sizeof(input)/sizeof(char); i++) {	
		if (input[i] == NULL) {
			break;
		}
		
		int tmp = input[i];	//��������  ��ȯ  
		while (1){
			binary[position] = tmp % 2;
			if(tmp%2 == 1) one_count++;
			tmp = tmp / 2;
			position ++;
			if (tmp == 0) break;
		}
		//even parity bit �߰�  
		if (one_count % 2 == 1) {
			binary[0] = 1;
		} else {
			binary[0] = 0;
		}
		//binary �迭 �������� �а� arr�� �߰��ϱ�   
		if (flag == 0) {
			for(int j = position-1; j >= 0; j--) {
				arr[j + ((input_size-1) * 8)] = binary[j];
			}
		} else {
			for(int j = position-1; j >= 0; j--) {
				arr[j + ((input_size) * 8)] = binary[j];
			}			
		}
		input_size --; 
		one_count = 0;
		position = 1;
	}

	char sum[16] = {0, };
	char checksum[16] = {0,};
	int j = 0;
	
	for (int i = (arr_size -1); i >= 0; i--) {
		checksum[j] = arr[i];
		j++;
		if(j == 16) {
			j = 0;
			//���ϱ�  
			for (int k = 15; k >= 0; k--) {
				if (sum[k] == 2 && k!= 0) {
					sum[k-1] += 1;
					sum[k] = 0;
				}
				if (sum[k] == 2 && k==0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
				sum[k] += checksum[k];
				if (sum[k] == 2 && k !=0 ) {
					sum[k-1] += 1;
					sum[k] = 0;
				}	
				if (sum[k] == 2 && k == 0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
			}	//ĳ�� �߻��Ѱ� ó��.	
			for (int k = 15; k >= 0; k--) {
				if (sum[k] == 2 && k!= 0) {
					sum[k-1] += 1;
					sum[k] = 0;
				}
				if (sum[k] == 2 && k==0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
				if (sum[k] == 2 && k !=0 ) {
					sum[k-1] += 1;
					sum[k] = 0;
				}	
				if (sum[k] == 2 && k == 0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
			}	//Ȥ�� �ѹ� �� ĳ�� �߻��Ѱ� ó �� 
			for (int k = 15; k >= 0; k--) {
				if (sum[k] == 2 && k!= 0) {
					sum[k-1] += 1;
					sum[k] = 0;
				}
				if (sum[k] == 2 && k==0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
				if (sum[k] == 2 && k !=0 ) {
					sum[k-1] += 1;
					sum[k] = 0;
				}	
				if (sum[k] == 2 && k == 0) {
					sum[15] += 1;
					sum[k] = 0; 
				}
			}		
		}
	}
	//���  
	for(int i = 0; i<16; i++) {	
		checksum[i] = !sum[i];
		printf("%d", checksum[i]);
	}

	return 0;
}
