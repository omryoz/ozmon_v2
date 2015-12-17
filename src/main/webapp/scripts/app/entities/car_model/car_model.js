'use strict';

angular.module('taxitraderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('car_model', {
                parent: 'entity',
                url: '/car_models',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'taxitraderApp.car_model.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car_model/car_models.html',
                        controller: 'Car_modelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car_model');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('car_model.detail', {
                parent: 'entity',
                url: '/car_model/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'taxitraderApp.car_model.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car_model/car_model-detail.html',
                        controller: 'Car_modelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car_model');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Car_model', function($stateParams, Car_model) {
                        return Car_model.get({id : $stateParams.id});
                    }]
                }
            })
            .state('car_model.new', {
                parent: 'car_model',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_model/car_model-dialog.html',
                        controller: 'Car_modelDialogController',
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
                        $state.go('car_model', null, { reload: true });
                    }, function() {
                        $state.go('car_model');
                    })
                }]
            })
            .state('car_model.edit', {
                parent: 'car_model',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_model/car_model-dialog.html',
                        controller: 'Car_modelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Car_model', function(Car_model) {
                                return Car_model.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('car_model', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('car_model.delete', {
                parent: 'car_model',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/car_model/car_model-delete-dialog.html',
                        controller: 'Car_modelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Car_model', function(Car_model) {
                                return Car_model.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('car_model', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
