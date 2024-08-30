#define bool _Bool
#include<stdlib.h>

int printf(const char *pattern, ...);
int scanf(const char *format, ...);
int sprintf(char *str, const char *format, ...);
void *malloc(unsigned int size);
char *strcat(char *dest, const char *src);
int strcmp(const char* str1, const char* str2);
unsigned int strlen(const char *str);
char* strcpy(char* str1, const char* str2);
void *memcpy(void *str1, const void *str2, unsigned int n);

void _print(char *str){
    printf("%s", str);
}

void _println(char *str){
    printf("%s\n",str);
}

void _printInt(int n){
    printf("%d",n);
}

void _printlnInt(int n){
    printf("%d\n",n);
}

char* _getString(){
    char* buffer=(char*)malloc(260);
    scanf("%s",&buffer[4]);
    int length=strlen(&buffer[4]);
    *((int*)buffer)=length;
    return &buffer[4];
}

int _getInt(){
    int n;
    scanf("%d",&n);
    return n;
}

char* _toString(int i){
    char* buffer=(char*)malloc(260);
    int length=sprintf(&buffer[4],"%d",i);
    *((int*)buffer)=length;
    return &buffer[4];
}

char* _string_substring(const char* str,int left,int right){
    int length=right-left;

    char* buffer=(char*)malloc(length+4);
    *((int*)buffer)=length;
    memcpy(&buffer[4],&str[left],length);
    buffer[length+4]='\0';
    return &buffer[4];
}

int _string_parseInt(const char* str){
    char curChar=*str;
    if(curChar=='\0')return 0;

    bool negative=0;
    if(curChar=='-'){
        negative=1;
        str++;
        curChar=*str;
    }

    int result=0;
    while(curChar>='0'&&curChar<='9'){
        int digit=curChar-'0';
        result=result*10+digit;
        str++;
        curChar=*str;
    }

    if(negative){
        result=-result;
    }

    return result;
}

int _string_ord(const char* str,int pos){
    return (int)(str[pos]);
}

char* _string_add(const char* lhs, const char* rhs){
    int length=strlen(lhs)+strlen(rhs);
    char *str=(char*)malloc(length+1+sizeof(int));
    *((int*)str)=length;
    str=&str[4];
    strcpy(str,lhs);
    strcat(str,rhs);
    return str;
}

bool _string_eq(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)==0;
}

bool _string_ne(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)!=0;
}

bool _string_le(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)<0;
}

bool _string_leq(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)<=0;
}

bool _string_ge(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)>0;
}

bool _string_geq(const char* lhs, const char* rhs){
    return strcmp(lhs,rhs)>=0;
}

char* _boolToString(bool i) {
    const char* true_str = "true";
    const char* false_str = "false";
    const char* str_to_copy;
    unsigned int len;

    if (i) {
        str_to_copy = true_str;
        len = strlen(true_str);
    } else {
        str_to_copy = false_str;
        len = strlen(false_str);
    }

    char* buffer = (char*)malloc(len + 1 + sizeof(int));
    *((int*)buffer) = len;
    memcpy(buffer + sizeof(int), str_to_copy, len);
    buffer[sizeof(int) + len - 1] = '\0';
    return buffer + sizeof(int);
}

char *_new_array(int size, int dimension, int *arraySize) {
    if (*arraySize == 0) return NULL;
    void *ptr;
    if (dimension == 1) {
        ptr = malloc(size * (*arraySize) + sizeof(int)) + sizeof(int);
        *(int *)(ptr - sizeof(int)) = *arraySize;
    }
    if (dimension > 1) {
        ptr = malloc(sizeof(int) * (*arraySize) + sizeof(int)) + sizeof(int);
        *(int *)(ptr - sizeof(int)) = *arraySize;
        for (int i = 0; i < *arraySize; i++)
            ((char **)ptr)[i] = _new_array(size, dimension - 1, arraySize + 1);
    }
    return (char *)ptr;
}