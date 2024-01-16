from django.shortcuts import render

from rest_framework.permissions import IsAuthenticated, AllowAny
from django.core.mail import send_mail, EmailMessage
from django.contrib.auth.models import User
from django.http import Http404

from rest_framework import viewsets, status, permissions
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.views import APIView

from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework_simplejwt.token_blacklist.models import BlacklistedToken, OutstandingToken

from .models import CustomUser
from .serializers import CustomUserSerializer, CustomUserCreateSerializer

import json
import requests


# Import variables found in settings.py
from oauth_example.settings import (
    GITHUB_CLIENT_SECRET,
    STACKOVERFLOW_CLIENT_SECRET
)

# Create your views here.
class UserDetail(APIView):
    permission_classes = [IsAuthenticated]
    # Require authentication for this endpoint
    def get_object(self, pk):
        try:
            return CustomUser.objects.get(pk=pk)
        except CustomUser.DoesNotExist:
            raise Http404
    
    def get(self, request, pk, format=None):
        user = self.get_object(request.user.id)
        serializer = CustomUserSerializer(user)
        return Response(serializer.data)
    
    # Update the user information
    def put(self, request, format=None):
        user = self.request.user

        # Check if email was passed
        email = request.data['email'] if 'email' in request.data else user.email

        # Check if first_name was passed
        first_name = request.data['first_name'] if 'first_name' in request.data else user.first_name

        # Check if last_name was passed
        last_name = request.data['last_name'] if 'last_name' in request.data else user.last_name

        


        updateData = {
            "email": email,
            "first_name": first_name,
            "last_name": last_name
        }

        serializer = CustomUserSerializer(user, data=updateData)
        if serializer.is_valid():
            # Save the serializer
            serializer.save()

            # If an image file is provided, handle it separately
            if 'profile_picture' in request.FILES:
                user.profile_picture = request.FILES['profile_picture']
                user.save()

            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class UserViewSet(viewsets.ModelViewSet):
    serializer_class = CustomUserCreateSerializer
    queryset = CustomUser.objects.all()

    def get_permissions(self):
        """
        Instantiates and returns the list of permissions that this view requires.
        """
        if self.action == 'create':
            permission_classes = [AllowAny]
        else:
            permission_classes = [IsAuthenticated]
        return [permission() for permission in permission_classes]

    def get_object(self, pk):
        try:
            return CustomUser.objects.get(pk=pk)
        except CustomUser.DoesNotExist:
            raise Http404

    def get_queryset(self):
        queryset = CustomUser.objects.all()
        return queryset

    def perform_create(self, serializer):
        serializer.save()#, created_by = self.request.user)



@api_view(['GET'])
@permission_classes([IsAuthenticated])
def get_account(request):
    # Try to get the account but if user isn't logged in return an error
    if request.user.is_authenticated:
        account = CustomUser.objects.filter(email__in=[request.user]).first()
        serializer = CustomUserSerializer(account)

        return Response(serializer.data)
    else:
        return Response(status=status.HTTP_401_UNAUTHORIZED)


class LogoutView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        try:
            refresh_token = request.data["refresh"]
            token = RefreshToken(refresh_token)
            token.blacklist()

            # Optionally, clear all tokens for the user
            user_tokens = OutstandingToken.objects.filter(user_id=request.user.id)
            for token in user_tokens:
                BlacklistedToken.objects.get_or_create(token=token)

            return Response(status=status.HTTP_205_RESET_CONTENT)
        except Exception as e:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        



# Get Github access token require authentication for this endpoint
@api_view(['POST'])
@permission_classes([IsAuthenticated])
def get_github_access_token(request):
    # Get client_id, code, and redirect_uri from request
    client_id = request.data['client_id']
    code = request.data['code']
    redirect_uri = request.data['redirect_uri']


    # Set the headers
    headers = {
        'Accept': 'application/json'
    }

    # Set the data
    data = {
        'client_id': client_id,
        'client_secret': GITHUB_CLIENT_SECRET,
        'code': code,
        'redirect_uri': redirect_uri
    }

    # Send the request
    response = requests.post('https://github.com/login/oauth/access_token', headers=headers, data=data)

    # Return the response
    return Response(response.json())


# Get Stackoverflow access token require authentication for this endpoint
@api_view(['POST'])
@permission_classes([IsAuthenticated])
def get_stackoverflow_access_token(request):

    print(request.data)

    # Get client_id, code, and redirect_uri from request
    client_id = request.data['client_id']
    code = request.data['code']
    redirect_uri = request.data['redirect_uri']


    # Set the headers
    headers = {
        'Accept': 'application/json'
    }

    # Set the data
    data = {
        'client_id': client_id,
        'client_secret': STACKOVERFLOW_CLIENT_SECRET,
        'code': code,
        'redirect_uri': redirect_uri
    }

    print(data)

    # Send the request
    response = requests.post('https://stackoverflow.com/oauth/access_token/json', headers=headers, data=data)

    try:
        print(f'response.json(): {response.json()}')
    except Exception as e:
        print(response)
        print(f'Exception: {e}')
        print(f'response.text: {response.text}')
        print(f'response.reason: {response.reason}')
        print(f'response.status_code: {response.status_code}')

    # Return the response
    return Response(response.json())


