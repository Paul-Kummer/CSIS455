#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

void factorialThread(void *varg)
{
	sleep(5);
	printf("Hello Thread \n");

	return 0;
}

void piThread(void *varg)
{

}

int main()
{
	pthread_t factorial;
	pthread_t pi;

	printf("Before Thread\n");

	pthread_create(&factorial, NULL, factorialThread, NULL);
	pthread_create(&pi, NULL, piThread, NULL);
	pthread_join(factorial, NULL);
	pthread_join(pi, NULL);

	printf("After Thread\n");

	return 0;
}