from django.shortcuts import render, HttpResponse
from django.views.decorators.cache import cache_page

# Create your views here.

@cache_page(60)
def index(request):
    return render(request,'index.html')


def generate_page(request):
    if request.method == "POST":
        intro = str(request.POST.get('intro'))
        if 'admin' in intro or 'config.' in intro:
            return HttpResponse("can't be as admin")
        outer_html = ('<h1>hello {user}</h1></p><h3>' + intro + '</h3>').format(user=request.user)
        f = request.FILES.get("file", None)
        filename = request.POST.get('filename') if request.POST.get('filename') else f.name
        if '.py' in filename:
            return HttpResponse("no py")
        if f is None:
            return HttpResponse("no file")
        else:
            with open("./app/static/{}".format(filename), 'wb') as ff:
                for chunk in f.chunks():
                    ff.write(chunk)
            return HttpResponse(outer_html + "</p><img src='/static/{}'>".format(filename))
    else:
        return HttpResponse("unable")
