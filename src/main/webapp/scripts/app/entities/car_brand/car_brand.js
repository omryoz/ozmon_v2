'use strict';

angular.module('taxitraderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('car_brand', {
                parent: 'entity',
                url: '/car_brands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'taxitraderApp.car_brand.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car_brand/car_brands.html',
                        controller: 'Car_brandController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car_brand');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('car_brand.detail', {
                parent: 'entity',
                url: '/car_brand/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'taxitraderApp.car_brand.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car_brand/car_brand-detail.html',
                        controller: 'Car_brandDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car_brand');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Car_brand', function($stateParams, Car_brand) {
                        return Car_brand.get({id : $stateParams.id});
                    }]
                }
            })
            .state('car_brand.new', {
                parent: 'car_brand',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_brand/car_brand-dialog.html',
                        controller: 'Car_brandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('car_brand', null, { reload: true });
                    }, function() {
                        $state.go('car_brand');
                    })
                }]
            })
            .state('car_brand.edit', {
                parent: 'car_brand',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_brand/car_brand-dialog.html',
                        controller: 'Car_brandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Car_brand', function(Car_brand) {
                                return Car_brand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('car_brand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('car_brand.delete', {
                parent: 'car_brand',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_brand/car_brand-delete-dialog.html',
                        controller: 'Car_brandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Car_brand', function(Car_brand) {
                                return Car_brand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('car_brand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
