#include <stdio.h>
#include <stdlib.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

#include <stdio.h>

int main() {
	
	FILE *fp = fopen("input.txt","r");
	char input[999999];	
	int binary[8];
	int position = 1;
	int one_count = 0;
	
	fread(input, sizeof(input) - 1, 1, fp);

	//�о�� �ؽ�Ʈ�� �� ���ڸ���  
	for (int i = 0; i < sizeof(input)/sizeof(char); i++) {	
		if (input[i] == NULL) {
			break;
		}
		
		int tmp = input[i];	//�ƽ�Ű�ڵ�� ��ȯ  
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
		//�迭 �������� �б�  
		for(int j = position-1; j >= 0; j--) {
			printf("%d",binary[j]);
		}
		printf("\n");
		one_count = 0;
		position = 1;
	}
	fclose(fp);
	return 0;
}
