char *__fastcall vm_create(__int64 a1, __int64 a2)
{
  char *v3; // [rsp+18h] [rbp-8h]

  v3 = (char *)malloc(168uLL);
  *(_DWORD *)v3 = 0;
  v3[4] = 0;
  *((_DWORD *)v3 + 40) = 0;
  memset(v3 + 8, 0, 0x80uLL);
  *((_QWORD *)v3 + 18) = calloc(0x10000uLL, 1uLL);
  memcpy(*((void **)v3 + 18), (const void *)(a1 + 3), a2 - 3);
  *((_QWORD *)v3 + 19) = calloc(0x200uLL, 4uLL);
  return v3;
}
