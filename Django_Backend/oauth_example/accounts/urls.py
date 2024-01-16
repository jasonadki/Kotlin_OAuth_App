from django.urls import path, include

from rest_framework.routers import DefaultRouter

# from rest_framework_simplejwt.views

from .views import *


router = DefaultRouter()
router.register('', UserViewSet, basename='accounts')

urlpatterns = [
    path('user/', UserDetail.as_view(), name='userdetail'),
    path('get_account/', get_account, name='get_account'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('get_github_access_token/', get_github_access_token, name='get_github_access_token'),
    path('get_stackoverflow_access_token/', get_stackoverflow_access_token, name='get_stackoverflow_access_token'),

    path('', include(router.urls)),
]
